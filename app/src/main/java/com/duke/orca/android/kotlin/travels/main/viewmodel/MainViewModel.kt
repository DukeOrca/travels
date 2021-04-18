package com.duke.orca.android.kotlin.travels.main.viewmodel

import androidx.lifecycle.*
import com.duke.orca.android.kotlin.travels.home.data.Tour
import com.duke.orca.android.kotlin.travels.main.repository.MainRepositoryImpl
import com.duke.orca.android.kotlin.travels.schedule.data.Schedule
import com.duke.orca.android.kotlin.travels.util.SingleLiveEvent
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

    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
            get() = _schedule

    fun setSchedule(value: Schedule) {
        _schedule.value = value
    }

    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>>
        get() = _schedules

    fun setSchedules(value: List<Schedule>) {
        _schedules.value = value
    }

    private var _currentSchedule: Schedule? = null
    val currentSchedule: Schedule?
        get() = _currentSchedule

    fun clearCurrentSchedule() {
        _currentSchedule = null
    }

    fun setCurrentSchedule(schedule: Schedule) {
        _currentSchedule = schedule
    }

    private val _navigateDetailFragmentEvent = SingleLiveEvent<Unit>()
    val navigateDetailFragmentEvent: SingleLiveEvent<Unit>
        get() = _navigateDetailFragmentEvent

    fun callNavigateDetailFragmentEvent() {
        _navigateDetailFragmentEvent.call()
    }

    private val _toggleAudioPlaybackStateEvent = SingleLiveEvent<Schedule>()
    val toggleAudioPlaybackStateEvent: SingleLiveEvent<Schedule>
        get() = _toggleAudioPlaybackStateEvent

    fun callToggleAudioPlaybackStateEvent(schedule: Schedule?) {
        schedule?.let {
            _toggleAudioPlaybackStateEvent.value = schedule
        } ?: run {
            _toggleAudioPlaybackStateEvent.call()
        }
    }

    // Dummy Data
    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainRepository.getData()?.let {
                    _tourlist.postValue(it.data.tourlist)
                }
            }
        }
    }

    init {
        setDummyData()
    }

    private fun setDummyData() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                setSchedules(
                    listOf(
                        Schedule(
                            address = "서울특별시",
                            audioUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                            detailedDescription =
                            """
                                반갑습니다.
                                
                                서울입니다.
                                서울입니다2.
                                서울입니다3.
                                서울입니다4.
                                서울입니다5.
                                서울입니다6.
                          
                            """.trimIndent(),
                            oneLineDescription = "서울한줄임.",
                            phoneNumber = "119",
                            tourimg = "https://cdn.pixabay.com/photo/2020/08/09/15/44/tower-5475850_1280.jpg",
                            tourspotname = "서울",
                            tourbegindate = "dates",
                            tourbegintime = "0시",
                            tourhour = 3),
                        Schedule(
                            address = "부산의 주소",
                            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                            detailedDescription =
                            """
                                안녕하세요.
                                
                                부산입니다.
                                
                                1
                                2
                                3
                                4
                                5
                              
                            """.trimIndent(),
                            oneLineDescription = "환영합니다.",
                            phoneNumber = "112",
                            tourimg = "https://cdn.pixabay.com/photo/2021/03/06/17/17/baby-turtle-6074231_1280.jpg",
                            tourspotname = "부산",
                            tourbegindate = "dateb",
                            tourbegintime = "1시",
                            tourhour = 3)
                    )
                )
            }
        }
    }
}