package com.example.dietnutrilyze.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dietnutrilyze.data.LoginActivity
import com.example.dietnutrilyze.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ambil data user dari Firestore dan set tv_nama dan tv_email
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userDocRef = firestore.collection("users").document(userId)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nama = document.getString("nama")
                        val email = firebaseAuth.currentUser?.email
                        binding.tvNama.text = nama
                        binding.tvEmail.text = email
                    } else {
                        // Handle jika dokumen tidak ada
                        binding.tvNama.text = "Nama tidak ditemukan"
                        binding.tvEmail.text = "Email tidak ditemukan"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle jika terjadi kesalahan saat mengambil data
                    binding.tvNama.text = "Gagal mengambil data: ${exception.message}"
                    binding.tvEmail.text = "Gagal mengambil data: ${exception.message}"
                }
        } else {
            binding.tvNama.text = "User tidak terautentikasi"
            binding.tvEmail.text = "User tidak terautentikasi"
        }

        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }
}
