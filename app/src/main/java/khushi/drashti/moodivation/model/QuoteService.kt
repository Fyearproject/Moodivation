package khushi.drashti.moodivation.model

import io.reactivex.Single
import khushi.drashti.moodivation.di.DaggerApiComponent
import javax.inject.Inject

class QuoteService {
    @Inject
    lateinit var api:QuoteApi

    init {
        DaggerApiComponent.create().inject(this)
    }
    fun getQuotes():Single<List<Quote>>{
        return api.getQuotes()
    }
}