package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityAccountSettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.custom_dialog.view.*

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.userEmailTV.text = snapshot.child("email").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })

        binding.deleteAccountBtn.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog, null)
            val dialog = Dialog(this)
            dialog.setContentView(dialogBinding)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialogBinding.titleName.text = "Вы действительно хотите удалить аккаунт?"
            dialogBinding.questionTV.visibility = View.GONE
            dialog.show()

            dialogBinding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.deleteBtn.setOnClickListener {
                userRef.removeValue().addOnSuccessListener {
                    FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {
                        StyleableToast.makeText(this, "Пользователь удален успешно!", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
                        startActivity(Intent(this, StartupScreenActivity::class.java))
                    }
                }
            }
        }

        binding.accountSettingGroupBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}