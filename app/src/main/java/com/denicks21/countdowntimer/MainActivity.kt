package com.denicks21.countdowntimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denicks21.countdowntimer.ui.theme.*
import com.denicks21.countdowntimer.viewmodels.TimerViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdownTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    val systemUiController = rememberSystemUiController()
                    val statusBarColor = MaterialTheme.colors.surface
                    val navigationBarColor = MaterialTheme.colors.onSurface
                    val barIcons = isSystemInDarkTheme()

                    SideEffect {
                        systemUiController.setNavigationBarColor(
                            color = navigationBarColor,
                            darkIcons = barIcons
                        )
                        systemUiController.setStatusBarColor(
                            color = statusBarColor,
                            darkIcons = true
                        )
                    }
                    Countdown()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Countdown(timerViewModel: TimerViewModel = viewModel()) {
    var showInfoDialog by remember { mutableStateOf(false) }
    val timerValue = timerViewModel.timerValue.value
    var isButtonPressed by remember { mutableStateOf(false) }
    val customHighlightText = TextSelectionColors(
        handleColor = DarkText,
        backgroundColor = DarkText
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showInfoDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) DarkText else LightText
                        )
                    }
                },
                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isButtonPressed) {
                Text(
                    text = timerValue,
                    color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(100.dp))
                CompositionLocalProvider(LocalTextSelectionColors provides customHighlightText) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {  },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        placeholder = {
                            Text(
                                text = "",
                                color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                                fontSize = 14.sp,
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                            focusedBorderColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            unfocusedBorderColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            cursorColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                }
            } else {
                OutlinedTextField(
                    value = timerValue,
                    onValueChange = { timerViewModel.timerValue.value = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = {
                        Text(
                            text = "Enter value",
                            color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            fontSize = 14.sp,
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                        backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                        focusedBorderColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                        cursorColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                    ),
                    modifier = Modifier.width(100.dp)
                )
            }
            if (timerValue == "0") {
                isButtonPressed = false
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        timerViewModel.startTimer(
                            timerValue.toLongOrNull()?.times(1000L) ?: 0L
                        )
                        isButtonPressed = true
                        timerViewModel.timerValue.value = ""
                        keyboardController?.hide()
                    },
                    backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    contentColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Outlined.PlayCircle,
                        contentDescription = "Start timer"
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                FloatingActionButton(
                    onClick = { timerViewModel.stopTimer() },
                    backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    contentColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Outlined.PauseCircle,
                        contentDescription = "Pause timer"
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                FloatingActionButton(
                    onClick = {
                        timerViewModel.resetTimer()
                        isButtonPressed = false
                    },
                    backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    contentColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Reset timer"
                    )
                }
                Spacer(modifier = Modifier.height(150.dp))
                if (timerViewModel.showError.value) {
                    AlertDialog(
                        onDismissRequest = { timerViewModel.showError.value = false },
                        backgroundColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                        title = {
                            Text(
                                text = "Error",
                                color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text(
                                text = "Please enter a valid number value",
                                color = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                                fontSize = 14.sp
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = { timerViewModel.showError.value = false },
                                colors = ButtonDefaults.buttonColors(backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey)
                            ) {
                                Text(
                                    text = "OK",
                                    color = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                                )
                            }
                        }
                    )
                }
            }
        }
    }
    if (showInfoDialog) {
        val uriHandler = LocalUriHandler.current

        Dialog(
            onDismissRequest = { showInfoDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .height(470.dp)
                    .width(450.dp),
                shape = RoundedCornerShape(size = 8.dp),
                backgroundColor = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { showInfoDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close dialog"
                        )
                    }
                    Card(
                        modifier = Modifier
                            .height(400.dp)
                            .width(450.dp)
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.onBackground,
                        elevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSystemInDarkTheme()) LightText else DarkText,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 2.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "#2 of 20 Android app ideas",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "This app show a countdown timer",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 2.dp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "My GitHub",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { uriHandler.openUri("https://github.com/ndenicolais") },
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.github_logo),
                                        contentDescription = "Open Github",
                                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) LightText else DarkText)
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed by DeNicks21",
                        color = if (isSystemInDarkTheme()) DarkText else LightText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}