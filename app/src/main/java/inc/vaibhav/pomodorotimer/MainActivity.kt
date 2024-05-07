package inc.vaibhav.pomodorotimer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import inc.vaibhav.pomodorotimer.ui.theme.PomodoroTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerTheme {
                Home()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    timerViewModel: TimerViewModel = viewModel()
) {
    val timerState by timerViewModel.timerState.collectAsState()

    val mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.sound)

    LaunchedEffect(key1 = timerState.remainingSeconds) {
        if (timerState.remainingSeconds == 0L)
            mediaPlayer.start()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val progress =
                timerState.remainingSeconds.toFloat() / (POMODORO_TIMER_SECONDS.toFloat())
            val minutes = timerState.remainingSeconds / SECONDS_IN_A_MINUTE
            val seconds = timerState.remainingSeconds - (minutes * SECONDS_IN_A_MINUTE)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.padding(bottom = Sizing.spacing16),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(Sizing.spacing256)
                            .height(Sizing.spacing256),
                        progress = progress
                    )
                    Text(
                        text = "Timer\n$minutes : $seconds",
                        modifier = Modifier.padding(bottom = Sizing.spacing8),
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.padding(bottom = Sizing.spacing8),
                        onClick = {
                            if (timerState.isPaused) {
                                timerViewModel.startTimer(timerState.remainingSeconds)
                            } else {
                                timerViewModel.stopTimer()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (timerState.isPaused) ImageVector.vectorResource(
                                id = R.drawable.baseline_pause_24
                            ) else Icons.Filled.PlayArrow, contentDescription = null
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(bottom = Sizing.spacing8),
                        onClick = {
                            timerViewModel.resetTimer(POMODORO_TIMER_SECONDS)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                    }
                }
            }
        }
    }
}

const val POMODORO_TIMER_SECONDS = 1500L
const val SECONDS_IN_A_MINUTE = 60L