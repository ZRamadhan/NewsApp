package com.and.news.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.and.news.data.database.UserDatabase
import com.and.news.data.entity.Users
import com.and.news.databinding.ActivitySignUpBinding
import com.and.news.ui.auth.SignInActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class SignUpActivity : AppCompatActivity(), SignUpView {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var presenter: SignUpPresenterImp
    private lateinit var users: Users
    private var database: UserDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        presenter = SignUpPresenterImp(this)
        setContentView(binding.root)

        database = UserDatabase.getInstance(this)

        binding.apply {

            btnSignUp.setOnClickListener {
                val username = inputUserName.text.toString()
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()

                if (presenter.checkEmpty(username, email, password)) {
                    users = Users(null, username, email, password)

                    GlobalScope.async {
                        val result = database?.userDao()?.insertUser(users)
                        runOnUiThread {
                            if (result != 0.toLong()) {
                                presenter.setSuccess(true)
                                goToSignIn()
                            } else presenter.setSuccess(false)
                        }
                    }
                }
            }

            btnHaveAccount.setOnClickListener {
                goToSignIn()
            }
        }
    }

    private fun goToSignIn() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun clearField() {
        binding.apply {
            inputUserName.clearFocus()
            inputEmail.clearFocus()
            inputPassword.clearFocus()
            inputUserName.text?.clear()
            inputEmail.text?.clear()
            inputPassword.text?.clear()
        }
    }
}