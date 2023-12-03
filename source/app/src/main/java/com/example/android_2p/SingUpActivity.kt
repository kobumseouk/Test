package com.example.android_2p

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_2p.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private val Name by lazy { findViewById<EditText>(R.id.userName) }
    private val DOB by lazy { findViewById<EditText>(R.id.DateOfBirth) }
    val UserEmail by lazy { findViewById<EditText>(R.id.userEmail) }
    private val Password by lazy { findViewById<EditText>(R.id.password) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singup)

        findViewById<Button>(R.id.signup).setOnClickListener {
            val name = Name?.text.toString()
            val dob = DOB?.text.toString()
            val userEmail = UserEmail?.text.toString()
            val password = Password?.text.toString()
            SignUp(userEmail, password)
        }

        auth = FirebaseAuth.getInstance()
    }

    private fun SignUp(userEmail: String, password: String) {
        auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 사용자 계정이 성공적으로 생성됨
                    val user = auth?.currentUser
                    startActivity(
                        Intent(this, FrebaseActivity::class.java)
                    )
                    finish()
                } else {
                    // 계정 생성이 실패하거나 오류가 발생함
                    val errorMessage = task.exception?.message ?: "User registration failed."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

}