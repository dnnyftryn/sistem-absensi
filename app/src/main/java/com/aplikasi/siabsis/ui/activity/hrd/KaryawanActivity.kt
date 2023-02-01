package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.data.model.RegisterUser
import com.aplikasi.siabsis.databinding.ActivityKaryawanBinding
import com.aplikasi.siabsis.ui.adapter.UserAdapter
import com.google.firebase.database.*

class KaryawanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaryawanBinding

    private lateinit var absenRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<RegisterUser>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKaryawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        absenRecyclerView = binding.recyclerViewKaryawan
        absenRecyclerView.layoutManager = LinearLayoutManager(this)
        absenRecyclerView.setHasFixedSize(true)
        userList = arrayListOf<RegisterUser>()

        getUserList()

    }

    private fun getUserList() {
        dbRef = FirebaseDatabase.getInstance().getReference("users")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()){
                    for (listUser in snapshot.children){
                        val userData = listUser.getValue(RegisterUser::class.java)
                        userList.add(userData!!)
                    }
                    val mAdapter = UserAdapter(userList)
                    absenRecyclerView.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}