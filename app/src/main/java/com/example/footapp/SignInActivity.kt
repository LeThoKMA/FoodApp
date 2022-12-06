package com.example.footapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnSignIn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initUI()
        initListener()
    }

    private fun initUI() {
        edtEmail = findViewById(R.id.edt_email_sign_in)
        edtPassword = findViewById(R.id.edt_password_sign_in)
        btnSignIn = findViewById(R.id.btn_sign_in)
    }

    private fun initListener() {
        btnSignIn!!.setOnClickListener { onClickSignIn() }
    }

    private fun onClickSignIn() {
        val mAuth = FirebaseAuth.getInstance()
        val email = edtEmail!!.text.toString().trim { it <= ' ' }
        val password = edtPassword!!.text.toString().trim { it <= ' ' }
        if (password.length < 6){
            Toast.makeText(applicationContext, "Mật khẩu phải đủ 6 kí tự!", Toast.LENGTH_LONG).show()
            return
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_LONG).show()
            }
        }
    }
}