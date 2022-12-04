package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import io.github.muddz.styleabletoast.StyleableToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailET.addTextChangedListener {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim {it <= ' '}).matches()) {
                binding.emailLayout.error = null
            }
        }

        binding.emailET.setOnFocusChangeListener {_, hasFocus ->
            if (!hasFocus) {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim {it <= ' '}).matches()) {
                    binding.emailLayout.error = "Некорректный адрес электронной почты"
                }
            }
        }

        binding.passET.addTextChangedListener {
            if (binding.passET.text.toString().length >= 4) {
                binding.passLayout.error = null
            }
        }

        binding.signinBtn.setOnClickListener {
            when {

                TextUtils.isEmpty(binding.emailET.text.toString().trim {it <= ' '}) -> {
                    binding.emailLayout.error = "Пожалуйста, введите адрес электронной почты"
                    binding.emailET.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim {it <= ' '}).matches() -> {
                    binding.emailLayout.error = "Некорректный адрес электронной почты"
                    binding.emailET.requestFocus()
                }

                TextUtils.isEmpty(binding.passET.text.toString().trim {it <= ' '}) -> {
                    binding.passLayout.error = "Пожалуйста, введите пароль"
                    binding.passET.requestFocus()
                }
                else -> {

                    val email: String = binding.emailET.text.toString().trim {it <= ' '}
                    val password: String = binding.passET.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                StyleableToast.makeText(
                                    this@LoginActivity,
                                    "Авторизация успешна.",
                                    Toast.LENGTH_SHORT,
                                    R.style.CustomToastStyle
                                ).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                                finish()
                            } else {
                                StyleableToast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT,
                                    R.style.CustomToastStyle
                                ).show()
                            }
                        }
                }
            }
        }

        binding.firstTimeBtn.setOnClickListener {
            onBackPressed()
        }

        binding.loginGroupBack.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}