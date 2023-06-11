package com.aplikasi.siabsis.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.data.model.Absen
import com.aplikasi.siabsis.data.model.RegisterUser
import com.aplikasi.siabsis.databinding.FragmentDashboardBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.adapter.AbsenAdapter
import com.aplikasi.siabsis.ui.adapter.UserAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dbRef: DatabaseReference
    private lateinit var pref: UserPreference
    private lateinit var list: ArrayList<Absen>
    private lateinit var absenRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = UserPreference(requireContext())
        list = ArrayList<Absen>()
        Log.d("TAG", "onViewCreated: ${pref.getNama()}")
        getListAbsensi(pref.getNama())
        absenRecyclerView = binding.recyclerViewAbsensi
        absenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        absenRecyclerView.setHasFixedSize(true)
    }

    private fun getListAbsensi(nama: String) {
        list.clear()
        dbRef = FirebaseDatabase.getInstance().getReference("absen")
        dbRef
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (listUser in snapshot.children){
                        val userData = listUser.getValue(Absen::class.java)
                        if (userData != null){
                            list.add(userData)
                        }
                        val mAdapter = AbsenAdapter(list)
                        absenRecyclerView.adapter = mAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}