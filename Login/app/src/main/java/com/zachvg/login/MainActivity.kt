package com.zachvg.login

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.zachvg.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // Start the gradient animation
        with(binding.constraintLayout.background as AnimationDrawable) {
            setEnterFadeDuration(3000)
            setExitFadeDuration(3000)
            start()
        }
    }

    override fun onStop() {
        super.onStop()

        // Stop the gradient animation
        (binding.constraintLayout.background as AnimationDrawable).stop()
    }
}