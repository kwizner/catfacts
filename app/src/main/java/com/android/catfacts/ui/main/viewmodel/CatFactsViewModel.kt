package com.android.catfacts.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.catfacts.data.repository.RepositoryImp
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.android.catfacts.core.network.Result
import com.android.catfacts.data.api.ApiFactory

class CatFactsViewModel : ViewModel() {

    private val parentJob = Job()


    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default


    private val scope = CoroutineScope(coroutineContext)
    private val repository = RepositoryImp(ApiFactory.api)


    val catFactsLiveData = MutableLiveData<MutableList<String>>()
    val errorLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun fetchCatFacts() {
        loadingLiveData.postValue(true)
        GlobalScope.launch {
            when (val result = repository.getCatFacts()) {
                is Result.Success -> {
                    errorLiveData.postValue(false)
                    catFactsLiveData.postValue(result.data)
                }
                is Result.Error -> {
                    errorLiveData.postValue(true)
                }
            }

            loadingLiveData.postValue(false)
        }
    }

    fun cancelAllRequest() = coroutineContext.cancel()
}