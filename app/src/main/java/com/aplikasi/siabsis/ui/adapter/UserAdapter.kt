package com.aplikasi.siabsis.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.model.RegisterUser

class UserAdapter(private val userList: ArrayList<RegisterUser>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.nama)
        val ttl: TextView = itemView.findViewById(R.id.ttl)
        val alamat: TextView = itemView.findViewById(R.id.alamat)
        val divisi: TextView = itemView.findViewById(R.id.divisi)
        val email: TextView = itemView.findViewById(R.id.email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nama.text = currentUser.namaLengkap
        holder.alamat.text = currentUser.alamat
        holder.ttl.text = currentUser.ttl
        holder.email.text = currentUser.email
        holder.divisi.text = currentUser.divisi
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}