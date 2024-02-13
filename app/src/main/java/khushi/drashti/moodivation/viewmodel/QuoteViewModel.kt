package khushi.drashti.moodivation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import khushi.drashti.moodivation.di.DaggerApiComponent
import khushi.drashti.moodivation.model.Quote
import khushi.drashti.moodivation.model.QuoteService
import khushi.drashti.moodivation.model.SearchQuoteService
import javax.inject.Inject

class QuoteViewModel: ViewModel() {
    var quote = MutableLiveData<List<Quote>>()
    var errorMessage = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    @Inject
    lateinit var quoteService: QuoteService

    @Inject
    lateinit var specificQuoteService: SearchQuoteService

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh(){
        fetchQuotes()
    }

    fun fetchSpecificQuotes(searchOptions:String){
        loading.value = true
        specificQuoteService.getSpecificQuote(searchOptions)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<Quote>>(){
                override fun onSuccess(value: List<Quote>?) {
                    quote.value = value
                    errorMessage.value = false
                    loading.value =false
                }

                override fun onError(e: Throwable?) {
                    errorMessage.value = true
                    loading.value = true
                }
            })
    }

    private fun fetchQuotes(){
        loading.value = true
        disposable.add(
            quoteService.getQuotes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Quote>>(){
                    override fun onSuccess(value: List<Quote>?) {
                        quote.value = value
                        errorMessage.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        errorMessage.value = true
                        loading.value = true
                    }
                })
        )
    }

}