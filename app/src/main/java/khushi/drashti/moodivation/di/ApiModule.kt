package khushi.drashti.moodivation.di

import dagger.Module
import dagger.Provides
import khushi.drashti.moodivation.model.QuoteApi
import khushi.drashti.moodivation.model.QuoteService
import khushi.drashti.moodivation.model.SearchQuoteApi
import khushi.drashti.moodivation.model.SearchQuoteService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://zenquotes.io/api/"
    private val SEARCH_URL = "https://raw.githubusercontent.com"

    @Provides
    fun provideQuotesApi():QuoteApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }

    @Provides
    fun provideQuotesService():QuoteService{
        return QuoteService()
    }

    @Provides
    fun providesSpecificQuotes(): SearchQuoteApi {
        return Retrofit.Builder()
            .baseUrl(SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(SearchQuoteApi::class.java)
    }

    @Provides
    fun provideSearchQuoteService(): SearchQuoteService {
        return SearchQuoteService()
    }
}