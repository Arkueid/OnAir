package com.arkueid.onair.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import com.arkueid.onair.R
import com.arkueid.onair.databinding.ActivityMainBinding
import com.arkueid.onair.ui.following.FollowingFragment
import com.arkueid.onair.ui.home.HomeFragment
import com.arkueid.onair.ui.settings.SettingsFragment
import com.arkueid.onair.ui.weekly.WeeklyFragment
import com.arkueid.onair.utils.ToastUtils
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var binding: ActivityMainBinding

    private val handler by lazy { Handler(mainLooper) }

    private var allowExit = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!allowExit) {
                ToastUtils.showContextToast(this@MainActivity, "再按一次退出应用")
                allowExit = true
                handler.postDelayed(2000) { allowExit = false }
            } else {
                finish()
            }
        }
    }

    private val settingsFragment by lazy { SettingsFragment() }

    private val followingFragment by lazy { FollowingFragment() }

    private val weeklyFragment by lazy { WeeklyFragment() }

    private val homeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createFragments()
        binding.bottomNavView.setOnItemSelectedListener(this)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        checkPermissions()
    }

    private fun createFragments() {
        with(supportFragmentManager) {
            beginTransaction().apply {
                add(R.id.playerFragment, homeFragment, HomeFragment.TAG)
                add(R.id.playerFragment, weeklyFragment, WeeklyFragment.TAG)
                hide(weeklyFragment)
                add(R.id.playerFragment, followingFragment, FollowingFragment.TAG)
                hide(followingFragment)
                add(R.id.playerFragment, settingsFragment, SettingsFragment.TAG)
                hide(settingsFragment)
                commit()
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            supportFragmentManager.fragments.forEach {
                if (it != fragment) {
                    hide(it)
                } else {
                    show(it)
                }
            }
            setReorderingAllowed(true)
            commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragmentHome -> showFragment(homeFragment)
            R.id.fragmentWeekly -> showFragment(weeklyFragment)
            R.id.fragmentFollowing -> showFragment(followingFragment)
            R.id.fragmentSettings -> showFragment(settingsFragment)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        onBackPressedCallback.remove()
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun goToSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 权限已授予，继续执行
            } else {
                ToastUtils.showToast("请在设置中开启权限")
                goToSetting()
            }
        }
    }
}