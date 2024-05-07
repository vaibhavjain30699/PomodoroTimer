package inc.vaibhav.pomodorotimer

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimerViewModel() : ViewModel() {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState = _timerState.asStateFlow()
    private var pomodoroTimer: CountDownTimer? = null

    init {
        _timerState.update {
            it.copy(
                remainingSeconds = POMODORO_TIMER_SECONDS
            )
        }
    }

    fun startTimer(seconds: Long) {
        _timerState.update {
            it.copy(
                isPaused = false
            )
        }
        pomodoroTimer = object : CountDownTimer(
            seconds * MILLISECONDS_IN_A_SECOND,
            MILLISECONDS_IN_A_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.update {
                    it.copy(
                        remainingSeconds = millisUntilFinished / MILLISECONDS_IN_A_SECOND
                    )
                }
            }

            override fun onFinish() {
                pomodoroTimer?.cancel()
                _timerState.update {
                    it.copy(
                        isPaused = true,
                        remainingSeconds = POMODORO_TIMER_SECONDS,
                    )
                }
            }
        }
        pomodoroTimer?.start()
    }

    fun stopTimer() {
        _timerState.update {
            it.copy(
                isPaused = true
            )
        }
        pomodoroTimer?.cancel()
    }

    fun resetTimer(seconds: Long) {
        pomodoroTimer?.cancel()
        _timerState.update {
            it.copy(
                isPaused = true,
                remainingSeconds = seconds
            )
        }
    }
}

const val MILLISECONDS_IN_A_SECOND = 1000L