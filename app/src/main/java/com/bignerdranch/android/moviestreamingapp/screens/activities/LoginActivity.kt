package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bignerdranch.android.moviestreamingapp.R
import com.google.firebase.auth.FirebaseAuth
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.emailET
import kotlinx.android.synthetic.main.activity_login.emailLayout
import kotlinx.android.synthetic.main.activity_login.passET
import kotlinx.android.synthetic.main.activity_login.passLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailET.addTextChangedListener() {
            if (Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString().trim {it <= ' '}).matches()) {
                emailLayout.error = null
            }
        }

        emailET.setOnFocusChangeListener {_, hasFocus ->
            if (!hasFocus) {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString().trim {it <= ' '}).matches()) {
                    emailLayout.error = "Некорректный адрес электронной почты"
                }
            }
        }

        passET.addTextChangedListener() {
            if (passET.text.toString().length >= 4) {
                passLayout.error = null
            }
        }

        signinBtn.setOnClickListener() {
            when {

                TextUtils.isEmpty(emailET.text.toString().trim {it <= ' '}) -> {
                    emailLayout.error = "Пожалуйста, введите адрес электронной почты"
                    emailET.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString().trim {it <= ' '}).matches() -> {
                    emailLayout.error = "Некорректный адрес электронной почты"
                    emailET.requestFocus()
                }

                TextUtils.isEmpty(passET.text.toString().trim {it <= ' '}) -> {
                    passLayout.error = "Пожалуйста, введите пароль"
                    passET.requestFocus()
                }
                else -> {

                    val email: String = emailET.text.toString().trim {it <= ' '}
                    val password: String = passET.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener() { task ->

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

        firstTimeRef.setOnClickListener() {
            onBackPressed()
        }

        loginGroupBack.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}