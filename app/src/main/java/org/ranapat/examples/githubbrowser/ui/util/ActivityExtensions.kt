package org.ranapat.examples.githubbrowser.ui.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.startRedirect(nextActivity: Class<out AppCompatActivity>) {
    val launchIntent = Intent()

    launchIntent.setClass(this, nextActivity)

    startActivity(launchIntent)

    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

fun AppCompatActivity.startCleanRedirect(nextActivity: Class<out AppCompatActivity>) {
    val launchIntent = Intent()
    launchIntent.setClass(this, nextActivity)
    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(launchIntent)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    finish()
}