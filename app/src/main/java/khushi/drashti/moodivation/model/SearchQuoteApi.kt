package khushi.drashti.moodivation.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchQuoteApi {

    @GET("Fyearproject/MyFiles/main/{type}.json")
    fun confidenceQuote(@Path("type")options:String):Single<List<Quote>>
}