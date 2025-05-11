package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun getEmailAndPassword(): Pair<String, String>? {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this@MainActivity, "Please enter email and password", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        return Pair(email, password)
    }

    fun signIn(view: View) {
        val receiveInfo = getEmailAndPassword() ?: return
        val (email, password) = receiveInfo

        if (password.length < 6) {
            Toast.makeText(
                this@MainActivity,
                "Password must be least 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


    fun signUp(view: View) {
        val receiveInfo = getEmailAndPassword() ?: return
        val (email, password) = receiveInfo

        if (password.length < 6) {
            Toast.makeText(
                this@MainActivity,
                "Password must be least 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }
}