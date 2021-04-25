package com.duke.orca.android.kotlin.travels.main.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.duke.orca.android.kotlin.travels.home.data.Tour
import com.duke.orca.android.kotlin.travels.main.repository.MainRepositoryImpl
import com.duke.orca.android.kotlin.travels.schedule.data.Location
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

    @Suppress("SpellCheckingInspection")
    fun refreshTourlist(@MainThread onComplete: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainRepository.getData()?.let {
                    _tourlist.postValue(it.data.tourlist)
                }
            }

            onComplete.invoke()
        }
    }

    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    fun setSchedule(value: Schedule) {
        _schedule.value = value
    }

    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>>
        get() = _schedules

    private fun setSchedules(value: List<Schedule>) {
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

    private val _mapSchedule = MutableLiveData<Schedule>()
    val mapSchedule: LiveData<Schedule>
        get() = _mapSchedule

    fun setMapSchedule(value: Schedule) {
        _mapSchedule.value = value
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

    private val _viewPager2UserInputEnabled = MutableLiveData<Boolean>()
    val viewPager2UserInputEnabled: LiveData<Boolean>
        get() = _viewPager2UserInputEnabled

    fun setViewPager2UserInputEnabled(value: Boolean) {
        _viewPager2UserInputEnabled.value = value
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
                            address = "고씨동굴",
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
                            tourLocation = Location(41.385064,2.173403),
                            pickUpLocation = Location(40.416775,-3.70379),
                            dropLocation = Location(41.648823,-0.889085),
                            tourimg = "https://cdn.pixabay.com/photo/2020/08/09/15/44/tower-5475850_1280.jpg",
                            tourspotname = "서울",
                            tourbegindate = "dates",
                            tourbegintime = "0시",
                            tourhour = 3,
                            videoUrl = "https://www.youtube.com/watch?v=-Ih5UArd4zk"),
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
                            tourLocation = Location(37.526376, 126.912035),
                            pickUpLocation = Location(37.528373, 126.911615),
                            dropLocation = Location(37.528976, 126.895138),
                            tourhour = 2,
                            videoUrl = "https://www.youtube.com/watch?v=tknKZe_TyqU")
                    )
                )
            }
        }
    }
}