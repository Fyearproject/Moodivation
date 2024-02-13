package khushi.drashti.moodivation.model

import io.reactivex.Single
import retrofit2.http.GET

interface QuoteApi {
    @GET("quotes")
    fun getQuotes(): Single<List<Quote>>
}