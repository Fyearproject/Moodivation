package khushi.drashti.moodivation.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import khushi.drashti.moodivation.R
import khushi.drashti.moodivation.model.Quote
import khushi.drashti.moodivation.util.getProgressDrawable
import khushi.drashti.moodivation.util.loadImage

class QuotesListAdapter(var quotes: ArrayList<Quote>, val context:Context): RecyclerView.Adapter<QuotesListAdapter.QuotesViewAdapter>(){
    fun updateQuotes(newQuotes: List<Quote>){
        quotes.clear()
        quotes.addAll(newQuotes)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewAdapter =
        QuotesViewAdapter(LayoutInflater.from(parent.context).inflate(R.layout.item_quote,parent,false))

    override fun onBindViewHolder(holder: QuotesViewAdapter, position: Int) {
        holder.bind(quotes[position])
        holder.parentLayout.setOnClickListener {
            val fragment: Fragment = OpenQuote(quotes[position])
            val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, fragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        }
        holder.parentLayout.setOnLongClickListener (View.OnLongClickListener {
            copyToClipboard(
                holder.quoteDialog.text.toString() + "\n -" + holder.quoteAuthor.text,
                holder.parentLayout
            )
            Toast.makeText(
                holder.parentLayout.context,
                "Copied quote to the clipboard",
                Toast.LENGTH_SHORT
            ).show()
            true
        })
    }

    private fun copyToClipboard(text: CharSequence, view:View){
        val clipboard = ContextCompat.getSystemService(view.context, ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("",text))
    }

    override fun getItemCount() = quotes.size

    class QuotesViewAdapter(view: View):RecyclerView.ViewHolder(view){
        val parentLayout: LinearLayout = view.findViewById(R.id.parent_layout)
        val quoteDialog: TextView = view.findViewById(R.id.quote_dialog)
        val quoteAuthor: TextView = view.findViewById(R.id.quote_author)

        fun bind(quote:Quote){
            quoteDialog.text = quote.quote
            quoteAuthor.text = String.format("- %s",quote.author)
        }
    }
}