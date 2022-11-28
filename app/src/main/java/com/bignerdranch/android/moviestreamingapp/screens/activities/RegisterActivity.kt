package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bignerdranch.android.moviestreamingapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.emailET
import kotlinx.android.synthetic.main.activity_register.passET

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailET.addTextChangedListener() {
            registerBtn.isEnabled = usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && passET.text.toString().trim {it <= ' '}.length >= 4

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

        usernameET.addTextChangedListener() {
            registerBtn.isEnabled = usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && passET.text.toString().trim {it <= ' '}.length >= 4
        }

        passET.addTextChangedListener() {
            registerBtn.isEnabled = usernameET.text.toString().trim {it <= ' '}.isNotEmpty()
                    && passET.text.toString().trim {it <= ' '}.length >= 4
        }

        dbRef = FirebaseDatabase.getInstance().reference.child("Users")
        registerBtn.setOnClickListener() {

            when {

                !Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString().trim {it <= ' '}).matches() -> {
                    emailLayout.error = "Некорректный адрес электронной почты"
                    emailET.requestFocus()
                }

                else -> {
                    val email: String = emailET.text.toString().trim {it <= ' '}
                    val username: String = usernameET.text.toString().trim {it <= ' '}
                    val password: String = passET.text.toString().trim {it <= ' '}

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
                                    //firebaseUserDb.child("profile_image").setValue("")

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

        cancelImage.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }
}