package com.arkueid.onair.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arkueid.onair.databinding.FragmentSettingsBinding
import com.arkueid.onair.ui.extension.ExtensionActivity

class SettingsFragment : Fragment() {

    companion object {
        const val TAG = "SettingsFragment"
    }

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.extension.setOnClickListener {
            openExtensionSetting()
        }
    }

    private fun openExtensionSetting() {
        val intent = Intent(requireContext(), ExtensionActivity::class.java)
        startActivity(intent)
    }
}