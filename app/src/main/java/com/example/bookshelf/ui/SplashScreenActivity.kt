package com.example.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivitySplashScreenBinding
import com.example.bookshelf.utils.PrefUtil

class SplashScreenActivity : AppCompatActivity() {

    private var binding : ActivitySplashScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        binding?.splashScreenImage?.startAnimation(slideAnimation)
        makeStatusBarTransparent()
        val isUserCurrentlyLoggedIn = PrefUtil.getInstance(this).isUserCurrentlyLoggedIn()


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            if(isUserCurrentlyLoggedIn) {
                val intent = Intent(this, BookShelfActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun makeStatusBarTransparent() {
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}
