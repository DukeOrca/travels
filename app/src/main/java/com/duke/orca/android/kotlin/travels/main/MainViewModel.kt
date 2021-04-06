package com.duke.orca.android.kotlin.travels.main

import androidx.lifecycle.*
import com.duke.orca.android.kotlin.travels.home.data.Tour
import com.duke.orca.android.kotlin.travels.main.repository.MainRepository
import com.duke.orca.android.kotlin.travels.main.repository.MainRepositoryImpl
import com.duke.orca.android.kotlin.travels.network.MainApi
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepositoryImpl): ViewModel() {
    @Suppress("SpellCheckingInspection")
    private val _tourlist = MutableLiveData<List<Tour>>()
    @Suppress("SpellCheckingInspection")
    val tourlist: LiveData<List<Tour>>
        get() = _tourlist

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainRepository.getData()?.let {
                    _tourlist.postValue(it.data.tourlist)
                }
            }
        }
    }
}