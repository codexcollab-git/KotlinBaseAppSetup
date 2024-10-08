package com.lokesh.appsetup.ui.module.xmlview.categories.view.activity

import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.BaseActivity
import com.lokesh.appsetup.databinding.ActivityCategoriesBinding
import com.lokesh.appsetup.util.NetworkManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>() {

    private lateinit var navController: NavController

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityCategoriesBinding = ActivityCategoriesBinding.inflate(
        layoutInflater
    )

    override fun initCreate() {
        super.initCreate()
        setupNavController()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun observeNetwork(isNetworkAvailable: Boolean) {
        super.observeNetwork(isNetworkAvailable)
        NetworkManager.internetAvailable = isNetworkAvailable
    }
}