package com.abhisek.rednit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.abhisek.rednit.R
import com.abhisek.rednit.viewmodel.ProfilesViewModel
import com.abhisek.rednit.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel  by lazy {
        ViewModelProvider(this, UserViewModel.Factory(application)).get(UserViewModel::class.java)
    }

    private val profileViewModel: ProfilesViewModel  by lazy {
        ViewModelProvider(this, ProfilesViewModel.Factory(application)).get(ProfilesViewModel::class.java)
    }

    private val appBarConfiguration:AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(
            R.id.loginFragment,
            R.id.mainFragment
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}