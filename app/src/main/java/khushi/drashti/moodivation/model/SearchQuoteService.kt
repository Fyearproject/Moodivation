package khushi.drashti.moodivation.model

import io.reactivex.Single
import khushi.drashti.moodivation.di.DaggerApiComponent
import javax.inject.Inject

class SearchQuoteService {
    @Inject
    lateinit var api:SearchQuoteApi

    init {
        DaggerApiComponent.create().inject(this)
    }
    fun getSpecificQuote(searchOptions:String):Single<List<Quote>>{
        return api.confidenceQuote(searchOptions)
    }
}