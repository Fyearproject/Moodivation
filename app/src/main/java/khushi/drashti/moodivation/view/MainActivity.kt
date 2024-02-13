package khushi.drashti.moodivation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import khushi.drashti.moodivation.R
import khushi.drashti.moodivation.viewmodel.QuoteViewModel
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup


class MainActivity : AppCompatActivity() {
    lateinit var quoteViewModel: QuoteViewModel
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var errorMessage: TextView? =null
    private var progressBar: ProgressBar? = null
    private val quotesAdapter = QuotesListAdapter(arrayListOf(),this)
    private var buttonGroup:ThemedToggleButtonGroup? = null
    //todo got the keywords from https://zenquotes.io/keywords
    private val quoteCategory:List<String> = listOf("Sad","Anxious","Failure","Confidence","Freedom",
            "Leadership","Love","Kindness","Time","Work","Success","Life","Pain")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        recyclerView = findViewById(R.id.list_item)
        errorMessage = findViewById(R.id.error_message)
        progressBar = findViewById(R.id.progressBar)
        buttonGroup = findViewById(R.id.quote_group)
        quoteViewModel = ViewModelProvider(this).get(QuoteViewModel::class.java)
        quoteViewModel.refresh()

        recyclerView!!.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quotesAdapter
        }
        for(buttonName in quoteCategory){
            val button = ThemedButton(buttonGroup!!.context)
            button.text = buttonName
            button.textColor = R.color.gray
            buttonGroup!!.addView(button,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ))
            val param = button.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,5,3,0)
            button.layoutParams = param
        }
        buttonGroup!!.setOnSelectListener { button: ThemedButton ->
            //todo change searchOptions to button.text once backend is ready.
            quoteViewModel.fetchSpecificQuotes(button.text.lowercase())
        }
        swipeRefreshLayout!!.setOnRefreshListener {
            val currentFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.fragment)
            if(currentFragment!=null && currentFragment.isResumed){
                swipeRefreshLayout!!.isRefreshing = false
            }else{
                swipeRefreshLayout!!.isRefreshing = false
                quoteViewModel.refresh()
            }
        }

        observeViewModel()
    }


    private fun observeViewModel(){
        quoteViewModel.quote.observe(this, Observer { quotes ->
            quotes?.let {
                recyclerView!!.visibility = View.VISIBLE
                quotesAdapter.updateQuotes(it)
            }
        })

        quoteViewModel.errorMessage.observe(this, Observer { isError ->
            isError?.let {
                errorMessage!!.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        quoteViewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                progressBar!!.visibility = if (it) View.VISIBLE else View.GONE
                if(it){
                    errorMessage!!.visibility = View.GONE
                    recyclerView!!.visibility = View.GONE
                }
            }
        })
    }
}