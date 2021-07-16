package com.asiasquare.byteg.shoppingdemo


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.databinding.ActivityMainBinding
import com.asiasquare.byteg.shoppingdemo.favorite.FavoriteFragmentViewModel
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository

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

        val viewModel: MainViewModel by lazy {
            val activity = requireNotNull(this)
            ViewModelProvider(this, MainViewModel.Factory(activity.application))
                .get(MainViewModel::class.java)
        }

        //Setup navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        viewModel.favoriteItemCount.observe(this, Observer {
            binding.bottomNav.getOrCreateBadge(R.id.favoriteFragment).apply {
                backgroundColor = ResourcesCompat.getColor(resources, R.color.secondary_500, null)
                badgeTextColor = ResourcesCompat.getColor(resources, R.color.white, null)
                maxCharacterCount = 3
                if (viewModel.favoriteItemCount.value != null && viewModel.favoriteItemCount.value!! >0 ) {
                    number = viewModel.favoriteItemCount.value!! // should be change
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        })


        //        viewModel.itemCount.observe(this, Observer  {
//            //override fun onChanged (){
//                binding.bottomNav.getOrCreateBadge(R.id.favoriteFragment).apply {
//                    backgroundColor = resources.getColor(R.color.secondary_800)
//                    badgeTextColor = resources.getColor(R.color.white)
//                    maxCharacterCount = 3
//                    if (viewModel.itemCount.value != null ) {
//                        number = viewModel.itemCount.value!! // should be change
//                        isVisible = true
//                    } else {
//                        isVisible = false
//                    }
//                }
//            //}
//
//        })

    }
}
