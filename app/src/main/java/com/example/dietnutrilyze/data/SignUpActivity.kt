package com.example.dietnutrilyze.data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dietnutrilyze.R
import com.example.dietnutrilyze.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout menggunakan view binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set onClickListener untuk tombol sign-in
        binding.signInButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Validasi input pengguna
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Buat pengguna baru dengan email dan password
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Pindah ke LoginActivity jika pendaftaran berhasil
                                Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, FirstDataActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Tampilkan pesan kesalahan jika pendaftaran gagal
                                Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Tampilkan pesan jika password tidak cocok
                    Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Tampilkan pesan jika ada kolom yang kosong
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignIn.setOnClickListener {
            // Navigate to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
