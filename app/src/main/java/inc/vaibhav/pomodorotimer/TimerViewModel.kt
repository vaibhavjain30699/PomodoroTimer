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
                remainingSeconds = 25L * 60L
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
            seconds * 1000,
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.update {
                    it.copy(
                        remainingSeconds = millisUntilFinished / 1000
                    )
                }
            }

            override fun onFinish() {
                pomodoroTimer?.cancel()
                _timerState.update {
                    it.copy(
                        isPaused = true
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