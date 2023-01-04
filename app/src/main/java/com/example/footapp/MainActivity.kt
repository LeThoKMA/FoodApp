package com.example.footapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.footapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding= DataBindingUtil.setContentView(this, R.layout.activity_main)



}
}