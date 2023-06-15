package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.data.model.Absen
import com.aplikasi.siabsis.databinding.ActivityDetailAbsenBinding
import com.aplikasi.siabsis.ui.adapter.AbsenAdapter
import com.google.firebase.database.*
import kotlin.math.abs

class DetailAbsenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAbsenBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var absenRecyclerView: RecyclerView
    private lateinit var list: ArrayList<Absen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        dbRef = FirebaseDatabase.getInstance().reference

        val intent = intent
        val absen = intent.getStringExtra("key")
        Log.d("TAG", "onCreate: $absen")

        getListAbsen(absen!!)

        absenRecyclerView = binding.recyclerViewAbsensi
        absenRecyclerView.layoutManager = LinearLayoutManager(this@DetailAbsenActivity)
        absenRecyclerView.setHasFixedSize(true)
        list = ArrayList<Absen>()
    }

    private fun getListAbsen(absen: String) {
        dbRef
            .child("absen")
            .child(absen)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val value = data.getValue(Absen::class.java)
                        list.add(value!!)
                        Log.d("TAG", "onDataChangevalue: $value")
                        binding.keterangan.text = list.size.toString()
                    }
                    absenRecyclerView.adapter = AbsenAdapter(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: ${error.message}")
                }

            })
    }
}