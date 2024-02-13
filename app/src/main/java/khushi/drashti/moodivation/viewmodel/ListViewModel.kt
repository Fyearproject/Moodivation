package khushi.drashti.moodivation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import khushi.drashti.moodivation.model.Quote

class ListViewModel:ViewModel() {
    var quotes = MutableLiveData<List<Quote>>()
    var errorMessage = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    private val disposable= CompositeDisposable()


}