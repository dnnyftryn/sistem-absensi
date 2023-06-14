package com.aplikasi.siabsis.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.model.Absen
import com.aplikasi.siabsis.data.model.DetailAbsen
import com.aplikasi.siabsis.ui.activity.hrd.DetailAbsenActivity

class KeyAdapter(private val key: ArrayList<String>) : RecyclerView.Adapter<KeyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val key = itemView.findViewById<TextView>(R.id.tv_key)
        val llKey = itemView.findViewById<View>(R.id.ll_key)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_key, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return key.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = key[position]
        holder.key.text = key

        holder.llKey.setOnClickListener {
            val detailAbsen = key

            val intent = Intent(holder.itemView.context, DetailAbsenActivity::class.java)
            intent.putExtra("key", detailAbsen)
            holder.itemView.context.startActivity(intent)
        }
    }

}