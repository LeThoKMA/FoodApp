package com.example.footapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.footapp.R
import com.example.footapp.interface1.LoginInterface
import com.example.footapp.model.User
import com.example.footapp.ui.home.HomeActivity
import com.example.footapp.presenter.LoginPresenter

class SignInActivity : AppCompatActivity(), LoginInterface {

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var list:ArrayList<User> = arrayListOf()
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        presenter = LoginPresenter(this, this)
        presenter.usersData.observe(this)
        {
            if(it!=null)
            {
                list.clear()
                list.addAll(it)
            }
        }
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
            presenter.validUser(
                edtEmail?.text.toString(),
                edtPassword?.text.toString(),
                list
            )
        }
    }

    override fun messageLogin(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun loginSuccess() {
             var intent=Intent(this,HomeActivity::class.java)

        startActivity(intent)
        finishAffinity()
    }


}