package com.aplikasi.siabsis.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.model.RegisterUser
import com.aplikasi.siabsis.pref.UserPreference
import com.google.firebase.database.FirebaseDatabase

class UserAdapter(private val userList: ArrayList<RegisterUser>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.nama)
        val ttl: TextView = itemView.findViewById(R.id.ttl)
        val alamat: TextView = itemView.findViewById(R.id.alamat)
        val divisi: TextView = itemView.findViewById(R.id.divisi)
        val email: TextView = itemView.findViewById(R.id.email)

        val btnHapus : Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        val query = dbRef.orderByChild("registerUserId").equalTo(userList[position].registerUserId)
        val pref = UserPreference(holder.itemView.context)
        val admin = pref.getUser()

        val currentUser = userList[position]
        holder.nama.text = currentUser.namaLengkap
        holder.alamat.text = currentUser.alamat
        holder.ttl.text = currentUser.ttl
        holder.email.text = currentUser.email
        holder.divisi.text = currentUser.divisi

        if (admin == "admin"){
            holder.btnHapus.visibility = View.VISIBLE
            holder.btnHapus.setOnClickListener {
                query.addValueEventListener(object : com.google.firebase.database.ValueEventListener{
                    override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                        for (dataSnapshot in snapshot.children){
                            dataSnapshot.ref.removeValue()
                        }
                    }

                    override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                        Log.d("TAG", "onCancelled: ${error.message}")
                    }

                })
            }
        } else {
            holder.btnHapus.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}