package com.example.footapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.model.User
import com.example.footapp.ui.Order.CartActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val handler = Handler()
        handler.postDelayed({ nextActivity() }, 2000)
    }

    private fun nextActivity() {
        var user = MyPreference().getInstance(this)?.getUser()
        Log.e("TAG", user.toString())
        val intent: Intent = if (user == User()) {
            Intent(this, SignInActivity::class.java)
        } else {
            Intent(this, CartActivity::class.java)
        }
        startActivity(intent)
        finishAffinity()
    }
}
