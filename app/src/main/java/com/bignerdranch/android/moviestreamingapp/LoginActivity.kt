package com.bignerdranch.android.moviestreamingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signinBtn.setOnClickListener() {
            when {
                TextUtils.isEmpty(emailET.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Пожалуйста, введите адрес электронной почты или логин.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(passET.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Пожалуйства введите пароль.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = emailET.text.toString().trim {it <= ' '}
                    val password: String = passET.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener() { task ->

                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Авторизация успешна.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

        firstTimeRef.setOnClickListener() {
            onBackPressed()
        }

        backGroup.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.nothing_x, R.anim.slide_out_right)
    }
}