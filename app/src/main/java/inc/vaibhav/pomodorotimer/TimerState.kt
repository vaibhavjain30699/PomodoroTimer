package inc.vaibhav.pomodorotimer

data class TimerState(
    val remainingSeconds: Long = 0L,
    val isPaused: Boolean = true,
)