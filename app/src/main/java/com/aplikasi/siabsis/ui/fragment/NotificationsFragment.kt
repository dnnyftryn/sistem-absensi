package com.aplikasi.siabsis.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aplikasi.siabsis.databinding.FragmentNotificationsBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.murid.LoginMuridActivity

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var pref: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = UserPreference(requireActivity())
        binding.textNotifications.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        pref.removeData()
        pref.editor?.clear()?.commit()
        (activity as AppCompatActivity).finish()
        startActivity(Intent(requireActivity(), LoginMuridActivity::class.java))
    }
}