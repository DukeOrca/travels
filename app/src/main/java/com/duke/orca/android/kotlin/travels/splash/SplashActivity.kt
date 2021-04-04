package com.duke.orca.android.kotlin.travels.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.duke.orca.android.kotlin.travels.databinding.ActivitySplashBinding
import com.duke.orca.android.kotlin.travels.entry.EntryActivity

class SplashActivity: AppCompatActivity() {

    private var viewBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        // TODO: ADD: Check login state.

        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, EntryActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }, 3000)
    }
}