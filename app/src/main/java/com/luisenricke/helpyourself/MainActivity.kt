package com.luisenricke.helpyourself

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.luisenricke.helpyourself.databinding.ActivityMainBinding
import com.luisenricke.helpyourself.service.Keyboard

//import com.luisenricke.helpyourself.databinding.ActivityMainBinding
//import com.luisenricke.helpyourself.service.Keyboard
//botonpanico
// https://developer.android.com/training/tv/playback/onboarding
class MainActivity : AppCompatActivity() {

    lateinit var keyboard: Keyboard

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        binding.apply {
            setContentView(root)

            // TODO: modify when solve the issue with FragmentContainerView
            // fragment
            navController = findNavController(R.id.fragment_host)
            // FragmentContainerView
            //        (supportFragmentManager.findFragmentById(binding.fragmentHost.id) as NavHostFragment)
            //            .navController

            appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_alert, R.id.nav_profile))

            bottom.setupWithNavController(navController)
        }

        keyboard = Keyboard.getInstance(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        if (isVisible) binding.bottom.visibility = View.VISIBLE
        else binding.bottom.visibility = View.GONE
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        keyboard.touchEvent(ev, currentFocus)
        return super.dispatchTouchEvent(ev)
    }
}
