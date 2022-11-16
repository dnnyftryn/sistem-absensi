package com.aplikasi.siabsis.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasi.siabsis.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSSigned.setOnClickListener {
            val email = binding.etSEmailAddress.text.toString()
            val password = binding.etSPassword.text.toString()
            val confirmPassword = binding.etSConfPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.etSEmailAddress.error = "Email tidak boleh kosong"
                binding.etSPassword.error = "Password tidak boleh kosong"
                binding.etSConfPassword.error = "Konfirmasi"
            }

            if (password != confirmPassword) {
                binding.etSPassword.error = "Password tidak sama"
                binding.etSConfPassword.error = "Password tidak sama"
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}