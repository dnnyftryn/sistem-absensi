package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.data.model.DetailAbsen
import com.aplikasi.siabsis.databinding.ActivityAbsenHrdBinding
import com.aplikasi.siabsis.ui.adapter.KeyAdapter
import com.google.firebase.database.*

class AbsenHrdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAbsenHrdBinding
    private lateinit var dbRef: DatabaseReference

    private var listAbsen = ArrayList<DetailAbsen>()
    private var list = ArrayList<String>()

    private lateinit var absenRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenHrdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        dbRef = FirebaseDatabase.getInstance().getReference("absen")
        absenRecyclerView = binding.recyclerViewAbsensi
        absenRecyclerView.layoutManager = LinearLayoutManager(this)
        absenRecyclerView.setHasFixedSize(true)
        listAbsen = arrayListOf<DetailAbsen>()

        getListAbsen()
    }

    private fun getListAbsen() {
        dbRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (absenSnapshot in snapshot.children) {
                        val absen = absenSnapshot.key
                        list.add(absen!!)
                        Log.d("TAG", "onDataChange: absen $list")
                        listAbsen.add(DetailAbsen(absen))
                        Log.d("TAG", "onDataChange: listAbsen $listAbsen")
                    }
                    val adapter = KeyAdapter(list)
                    absenRecyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AbsenHrdActivity, error.message, Toast.LENGTH_SHORT).show()

                }

            })
    }
}