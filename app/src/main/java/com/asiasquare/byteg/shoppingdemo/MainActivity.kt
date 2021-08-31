package com.asiasquare.byteg.shoppingdemo

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.asiasquare.byteg.shoppingdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Using viewBinding for activity
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Setup navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)


        /**
         * Show Favorite Badge
         */
        viewModel.favoriteItemCount.observe(this, {
            binding.bottomNav.getOrCreateBadge(R.id.favoriteFragment).apply {
                backgroundColor = ResourcesCompat.getColor(resources, R.color.secondary_500, null)
                badgeTextColor = ResourcesCompat.getColor(resources, R.color.white, null)
                maxCharacterCount = 3
                if (viewModel.favoriteItemCount.value != null && viewModel.favoriteItemCount.value!! >0 ) {
                    number = viewModel.favoriteItemCount.value!!
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        })

        /**
         * Show Shopping Basket Badge
         */
        viewModel.cartAmountItemCount.observe(this, {
            binding.bottomNav.getOrCreateBadge(R.id.cartFragment).apply {
                backgroundColor = ResourcesCompat.getColor(resources, R.color.secondary_500, null)
                badgeTextColor = ResourcesCompat.getColor(resources, R.color.white, null)
                maxCharacterCount = 3
                if (viewModel.cartAmountItemCount.value != null && viewModel.cartAmountItemCount.value!! >0 ) {
                    number = viewModel.cartAmountItemCount.value!!
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



}

