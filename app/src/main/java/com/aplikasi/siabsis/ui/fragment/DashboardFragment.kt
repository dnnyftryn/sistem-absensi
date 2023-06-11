package com.aplikasi.siabsis.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aplikasi.siabsis.data.model.Absen
import com.aplikasi.siabsis.databinding.FragmentDashboardBinding
import com.aplikasi.siabsis.pref.UserPreference
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
        dbRef = FirebaseDatabase.getInstance().getReference("Absen")
        pref = UserPreference(requireContext())
        getListAbsensi(pref.getNama())
    }

    private fun getListAbsensi(nama: String) {
        dbRef
            .child("absen")
            .child(nama)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Absen>()
                    for (data in snapshot.children) {
                        val absen = data.getValue(Absen::class.java)
                        absen?.let { list.add(it) }
                        Log.d("TAG", "onDataChange: $absen")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: ${error.message}")
                }

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}