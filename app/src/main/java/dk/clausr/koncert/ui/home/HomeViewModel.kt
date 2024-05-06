package dk.clausr.koncert.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.clausr.core.models.UserData
import dk.clausr.repo.concerts.ConcertRepository
import dk.clausr.repo.domain.Message
import dk.clausr.repo.userdata.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val supabase: SupabaseClient,
    private val realtimeChannel: RealtimeChannel,
    private val concertRepo: ConcertRepository,
) : ViewModel() {

    val messages = MutableStateFlow<List<Message>>(emptyList())

    override fun onCleared() {
        super.onCleared()
        disconnectFromRealtime()
    }


    fun connectToRealtime() = viewModelScope.launch {
        realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
            table = "rartis"
        }.onEach {
            when (it) {
                is PostgresAction.Delete -> messages.value =
                    messages.value.filter { message -> message.id != it.oldRecord["id"]!!.jsonPrimitive.int }

                is PostgresAction.Insert -> messages.value += it.decodeRecord<Message>()
                is PostgresAction.Select -> error("Select should not be possible")
                is PostgresAction.Update -> error("Update should not be possible")
            }
        }
        realtimeChannel.subscribe()
//            .launchIn()
    }

    fun disconnectFromRealtime() = viewModelScope.launch {
        supabase.realtime.disconnect()
    }

    val userData: StateFlow<UserData?> = userRepository.getUserData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun setData(username: String, group: String) = viewModelScope.launch {
        userRepository.setUserData(UserData(username, group))
    }

//    val someMessages: StateFlow<List<Message>> = flow<List<Message>> {
//        emit(concertRepo.retrieveMessages().getOrNull() ?: emptyList<Message>())
//    }.stateIn(
//        viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = emptyList()
//    )

    fun getMessages() = viewModelScope.launch {
        val res = concertRepo.retrieveMessages()
        if (res.isSuccess) {
            messages.value = res.getOrNull() ?: emptyList()
            Timber.d("MEssages: ${res.getOrNull()} -- $res")
        }
    }

    fun sendMessage() = viewModelScope.launch {
        concertRepo.createMessage("Message here hello")
    }
}