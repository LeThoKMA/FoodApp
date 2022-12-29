package com.example.footapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.footapp.ui.home.HomeActivity
import com.example.footapp.ui.home.HomeAdapter
import com.example.footapp.ui.login.LoginInterface
import com.example.footapp.ui.login.LoginPresenter
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity(), LoginInterface {

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnSignIn: Button? = null
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        presenter = LoginPresenter(this, this)
        initUI()
        initListener()
    }

    private fun initUI() {
        edtEmail = findViewById(R.id.edt_email_sign_in)
        edtPassword = findViewById(R.id.edt_password_sign_in)
        btnSignIn = findViewById(R.id.btn_sign_in)
    }

    private fun initListener() {
        btnSignIn!!.setOnClickListener {
            presenter.signIn(
                edtEmail?.text.toString(),
                edtPassword?.text.toString()
            )
        }
    }

    override fun messageLogin(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun loginSuccess() {
             var intent=Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }


}