package com.aplikasi.siabsis.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aplikasi.siabsis.databinding.FragmentHomeBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.activity.AbsenActivity
import com.aplikasi.siabsis.ui.activity.hrd.KaryawanActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var pref: UserPreference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = UserPreference(requireContext())

//        val email = requireArguments().getString("email")
        binding.tvEmail.text = pref.getUser()
        binding.cardViewTeam.setOnClickListener {
            startActivity(Intent(requireActivity(), KaryawanActivity::class.java))
        }
        binding.cardViewAbsenMasuk.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("absen", "Absen Masuk")
            val intent = Intent(requireActivity(), AbsenActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

        }

        binding.cardViewAbsenKeluar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("absen", "Absen Keluar")
            val intent = Intent(requireActivity(), AbsenActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.cardViewAnnouncement.setOnClickListener {

        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}