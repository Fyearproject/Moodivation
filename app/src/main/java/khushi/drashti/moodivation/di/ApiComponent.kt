package khushi.drashti.moodivation.di

import dagger.Component
import khushi.drashti.moodivation.model.QuoteService
import khushi.drashti.moodivation.model.SearchQuoteApi
import khushi.drashti.moodivation.model.SearchQuoteService
import khushi.drashti.moodivation.viewmodel.QuoteViewModel

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: QuoteService)

    fun inject(viewModel: QuoteViewModel)

    fun inject(service:SearchQuoteService)




}