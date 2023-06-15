package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasi.siabsis.data.model.RegisterUser
import com.aplikasi.siabsis.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("users")

        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSSigned.setOnClickListener {
            val email = binding.etSEmailAddress.text.toString()
            val password = binding.etSPassword.text.toString()
            val confirmPassword = binding.etSConfPassword.text.toString()

            val namaLengkap = binding.etNamaLengkap.text.toString()
            val ttl = binding.etTempatTanggalLahir.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val divisi = binding.etDivisi.text.toString()

            when {
                (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) -> {
                    binding.etSEmailAddress.error = "Email tidak boleh kosong"
                    binding.etSPassword.error = "Password tidak boleh kosong"
                    binding.etSConfPassword.error = "Konfirmasi"
                }
                (password != confirmPassword) -> {
                    binding.etSPassword.error = "Password tidak sama"
                    binding.etSConfPassword.error = "Password tidak sama"
                }
                else -> {
                    registerUser(email, password)
                    daftarUser(namaLengkap, ttl, alamat, email, divisi)
                }
            }
        }

    }

    private fun daftarUser(namaLengkap: String, ttl: String, alamat: String, email: String, divisi: String) {
        val registerUserId = dbRef.push().key.toString()

        val registerUser = RegisterUser(registerUserId, namaLengkap, ttl, alamat, email, divisi)

        dbRef.child(registerUserId).setValue(registerUser)
            .addOnCompleteListener {
                Snackbar.make(
                    binding.root,
                    "Data berhasil ditambahkan",
                    Snackbar.LENGTH_LONG)
                    .show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(
                    binding.root,
                    "Data Gagal ditambahkan",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
    }

    private fun registerUser(email: String, password: String) {
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