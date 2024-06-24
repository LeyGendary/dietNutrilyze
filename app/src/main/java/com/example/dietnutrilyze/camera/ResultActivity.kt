package com.example.dietnutrilyze.camera

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dietnutrilyze.databinding.ActivityResultBinding
import com.example.dietnutrilyze.network.PredictionResponse
import com.example.dietnutrilyze.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = Uri.parse(imageUriString)

        binding.ivResult.setImageURI(imageUri)

        val filePath = getFilePathFromUri(imageUri)
        if (filePath != null) {
            val file = File(filePath)
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            RetrofitClient.instance.predictImage(body).enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            binding.tvResultText.text = it.data.result
                            binding.tvCaloriesText.text = "Calories: ${it.data.Calories} kkal"
                        }
                    } else {
                        Toast.makeText(this@ResultActivity, "Failed to get response", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ResultActivity", "Error: ${t.message}")
                }
            })
        } else {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = cursor.getString(columnIndex)
            }
        }

        // Jika cursor tidak menemukan data, coba ambil path dari URI
        if (filePath == null) {
            filePath = uri.path
        }

        return filePath
    }
}
