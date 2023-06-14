package com.aplikasi.siabsis.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplikasi.siabsis.databinding.ActivityLoginBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.activity.hrd.HRDActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        pref = UserPreference(this)


        binding.btnSLogin.setOnClickListener {
            val email = binding.etSEmailAddress.text.toString().trim()
            val password = binding.etSPassword.text.toString().trim()
            when{
                email.isEmpty() -> binding.etSEmailAddress.error = "Email tidak boleh kosong"
                password.isEmpty() -> binding.etSPassword.error = "Password tidak boleh kosong"
                else -> {
                    loginUser(email, password)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                when{
                    (task.isSuccessful) -> {
                        val email = task.result.user!!.email
                        pref.setUser(email)
                        pref.setLogin(true)
                        pref.setUid(task.result.user!!.uid)
                        Toast.makeText(this, "Selamat datang ${task.result.user!!.email}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    (email == "admin" && password == "admin") -> {
                        pref.setLogin(true)
                        pref.setUser("admin")
                        Toast.makeText(this, "Selamat datang HRD", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HRDActivity::class.java))
                        finish()

                    } else -> {
                        Toast.makeText(this, "email dan password salah", Toast.LENGTH_LONG).show()
                    }
                }
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Selamat datang ${task.result.user}", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Selamat datang HRD", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, HRDActivity::class.java))
//                    finish()
//                }
            }
    }
}