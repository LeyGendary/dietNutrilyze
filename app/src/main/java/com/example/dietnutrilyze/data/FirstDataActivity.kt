package com.example.dietnutrilyze.data

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dietnutrilyze.databinding.ActivityFirstDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstDataBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout menggunakan view binding
        binding = ActivityFirstDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set onClickListener untuk tombol submit
        binding.button.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        val nama = binding.etNama.text.toString()
        val usia = binding.etUsia.text.toString()
        val beratBadan = binding.etBb.text.toString()
        val tinggiBadan = binding.etTb.text.toString()
        val jenisKelamin = if (binding.maleRadioButton.isChecked) {
            "Laki-laki"
        } else if (binding.femaleRadioButton.isChecked) {
            "Perempuan"
        } else {
            ""
        }

        if (nama.isNotEmpty() && usia.isNotEmpty() && beratBadan.isNotEmpty() && tinggiBadan.isNotEmpty() && jenisKelamin.isNotEmpty()) {
            val userId = firebaseAuth.currentUser?.uid
            val userMap = hashMapOf(
                "nama" to nama,
                "usia" to usia.toInt(),
                "beratBadan" to beratBadan.toInt(),
                "tinggiBadan" to tinggiBadan.toInt(),
                "jenisKelamin" to jenisKelamin
            )

            userId?.let {
                firestore.collection("users").document(it)
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
        }
    }
}
