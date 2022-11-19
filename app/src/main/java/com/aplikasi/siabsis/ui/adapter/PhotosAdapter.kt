package com.aplikasi.siabsis.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.ResultUserResponse
import com.aplikasi.siabsis.databinding.ItemPhotoBinding

class PhotosAdapter(val data: List<ResultUserResponse>?) : RecyclerView.Adapter<PhotosAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(data?.get(position))
    }

    inner class MyHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(get: ResultUserResponse?) {
            binding.nama.text = get?.name
            binding.email.text = get?.email
            binding.notelp.text = get?.phone
            val address = "${get?.address?.street}, ${get?.address?.suite}, ${get?.address?.city}, ${get?.address?.zipcode}"
            binding.alamat.text = address
        }
    }
}