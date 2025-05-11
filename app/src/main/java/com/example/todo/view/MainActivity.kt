package com.example.todo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        // FirebaseAuth initialize
        auth = Firebase.auth

        // Kullanıcı daha önceden giriş yaptıysa doğrudan HomeActivity'e yönlendir
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Email ve şifre alanlarını kontrol et
    fun getEmailAndPassword(): Pair<String, String>? {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        // Eğer alanlar boşsa null dön ve uyarı ver
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this@MainActivity, "Please enter email and password", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        return Pair(email, password)
    }

    // Kullanıcı giriş yap fonksiyonu
    fun signIn(view: View) {
        val receiveInfo = getEmailAndPassword() ?: return
        val (email, password) = receiveInfo

        // Şifre minimum 6 karakter değilse uyarı göster
        if (password.length < 6) {
            Toast.makeText(
                this@MainActivity,
                "Password must be least 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Firebase üzerinden giriş işlemi
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Giriş başarılıysa HomeActivity'e yönlendir
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                // Giriş başarısızsa hata mesajını göster
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    // Yeni kullanıcı kaydı oluştur
    fun signUp(view: View) {
        val receiveInfo = getEmailAndPassword() ?: return
        val (email, password) = receiveInfo

        // Şifre uzunluğu kontrolü
        if (password.length < 6) {
            Toast.makeText(
                this@MainActivity,
                "Password must be least 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Firebase üzerinden yeni kullanıcı oluşturma
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Kayıt başarılıysa HomeActivity'e yönlendir
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                // Kayıt başarısızsa hata mesajını göster
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}
