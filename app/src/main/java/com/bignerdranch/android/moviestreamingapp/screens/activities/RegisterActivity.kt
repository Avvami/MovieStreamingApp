package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.muddz.styleabletoast.StyleableToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailET.addTextChangedListener {
            binding.registerBtn.isEnabled = binding.usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && binding.passET.text.toString().trim {it <= ' '}.length >= 4

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

        binding.usernameET.addTextChangedListener {
            binding.registerBtn.isEnabled = binding.usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && binding.passET.text.toString().trim {it <= ' '}.length >= 4
        }

        binding.passET.addTextChangedListener {
            binding.registerBtn.isEnabled = binding.usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && binding.passET.text.toString().trim {it <= ' '}.length >= 4
        }

        dbRef = FirebaseDatabase.getInstance().reference.child("Users")
        binding.registerBtn.setOnClickListener {

            when {

                !Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim {it <= ' '}).matches() -> {
                    binding.emailLayout.error = "Некорректный адрес электронной почты"
                    binding.emailET.requestFocus()
                }

                else -> {
                    val email: String = binding.emailET.text.toString().trim {it <= ' '}
                    val username: String = binding.usernameET.text.toString().trim {it <= ' '}
                    val password: String = binding.passET.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                //If successfully registered
                                if (task.isSuccessful) {
                                    //Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    val firebaseUserDb = dbRef.child((firebaseUser.uid))
                                    firebaseUserDb.child("email").setValue((firebaseUser.email))
                                    firebaseUserDb.child("username").setValue(username)

                                    StyleableToast.makeText(
                                        this@RegisterActivity,
                                        "Аккаунт успешно создан",
                                        Toast.LENGTH_SHORT,
                                        R.style.CustomToastStyle
                                    ).show()

                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                                    finish()
                                } else { //Not successful registered
                                    StyleableToast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT,
                                        R.style.CustomToastStyle
                                    ).show()
                                }
                            }
                        )
                }
            }
        }

        binding.cancelImage.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }
}