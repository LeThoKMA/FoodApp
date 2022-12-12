package com.example.footapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.footapp.model.User
import com.example.footapp.utils.API_DATABASE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database= FirebaseDatabase.getInstance(API_DATABASE).reference
       // database = Firebase.database.reference

      database.child("users").child("1").get().addOnSuccessListener {
          Log.e("firebase", "Got value ${it.value}")
      }.addOnFailureListener{
          Log.e("firebase", "Error getting data", it)
      }
      }

}