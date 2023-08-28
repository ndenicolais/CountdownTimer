package com.denicks21.countdowntimer.viewmodels

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private lateinit var countDownTimer: CountDownTimer
    val timerValue = mutableStateOf("")
    val showError = mutableStateOf(false)

    // Start the timer
    fun startTimer(durationInMillis: Long) {
        if (durationInMillis <= 0) {
            showError.value = true
            return
        }
        showError.value = false
        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerValue.value = seconds.toString()
            }

            override fun onFinish() {
                timerValue.value = ""
            }
        }
        countDownTimer.start()
    }

    // Stop the timer
    fun stopTimer() {
        countDownTimer.cancel()
    }

    // Reset the timer
    fun resetTimer() {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        timerValue.value = ""
    }
}