package com.aplikasi.siabsis.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.siabsis.API.RetrofitClient
import com.aplikasi.siabsis.data.ResultUserResponse
import com.aplikasi.siabsis.databinding.ActivityLogin2Binding
import com.aplikasi.siabsis.ui.adapter.PhotosAdapter
import retrofit2.Call
import retrofit2.Response

class LoginActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding

    private lateinit var adapter: PhotosAdapter

    private var users : List<ResultUserResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PhotosAdapter(users)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)

//        RetrofitClient.instance.getUsers()
//            .enqueue(object : retrofit2.Callback<List<ResultUserResponse>> {
//                override fun onResponse(
//                    call: Call<List<ResultUserResponse>>,
//                    response: Response<List<ResultUserResponse>>
//                ) {
//                    binding.rvUser.adapter = PhotosAdapter(response.body())
//                }
//
//                override fun onFailure(call: Call<List<ResultUserResponse>>, t: Throwable) {
//                    Toast.makeText(this@LoginActivity2, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//            })
    }
}