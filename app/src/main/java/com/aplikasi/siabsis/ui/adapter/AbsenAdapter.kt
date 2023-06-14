package com.aplikasi.siabsis.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.model.Absen

class AbsenAdapter(private val absen: ArrayList<Absen>) : RecyclerView.Adapter<AbsenAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullTanggal = itemView.findViewById<TextView>(R.id.tanggal)
        val jamMasuk = itemView.findViewById<TextView>(R.id.jamMasuk)
        val jamPulang = itemView.findViewById<TextView>(R.id.jamPulang)
        val status = itemView.findViewById<TextView>(R.id.status)
        val nama = itemView.findViewById<TextView>(R.id.nama)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_absen, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return absen.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAbsen = absen[position]
        holder.fullTanggal.text = currentAbsen.tanggal
        holder.jamMasuk.text = currentAbsen.tanggal_masuk
        holder.jamPulang.text = currentAbsen.tanggal_keluar
        holder.status.text = currentAbsen.keterangan
        holder.nama.text = currentAbsen.nama
    }
}