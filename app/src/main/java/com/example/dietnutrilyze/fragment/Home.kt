package com.example.dietnutrilyze.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dietnutrilyze.R
import com.example.dietnutrilyze.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ambil data user dari Firestore dan set tv_nama
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userDocRef = firestore.collection("users").document(userId)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nama = document.getString("nama")
                        binding.tvNama.text = nama
                    } else {
                        // Handle jika dokumen tidak ada
                        binding.tvNama.text = "Nama tidak ditemukan"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle jika terjadi kesalahan saat mengambil data
                    binding.tvNama.text = "Gagal mengambil data: ${exception.message}"
                }
        } else {
            binding.tvNama.text = "User tidak terautentikasi"
        }
    }
}
