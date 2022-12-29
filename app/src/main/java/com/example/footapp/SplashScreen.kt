package com.example.footapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.footapp.model.User
import com.example.footapp.ui.MyPreference
import com.example.footapp.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({ nextActivity() }, 2000)
    }

    private fun nextActivity() {
        var user=MyPreference().getInstance(this)?.getUser()
        Log.e("TAG", user.toString())
        val intent: Intent = if (user == User()) {
            Intent(this, SignInActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}