package com.and.news.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.and.news.R
import com.and.news.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHaveAccount.setOnClickListener {
            Intent(this, SignInActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }
}