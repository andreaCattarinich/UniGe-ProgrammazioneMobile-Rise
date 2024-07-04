package com.unige.rise

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.unige.rise.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        // Remove title in Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_quiz,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Logout
        val menu = navView.menu
        val logoutItem = menu.findItem(R.id.nav_logout)
        logoutItem.setOnMenuItemClickListener {
            showAlertDialogLogout()
            true
        }


        // Add name, surname and email in the nav_header
        val headerView = navView.getHeaderView(0)
        val nameSurname = headerView.findViewById<TextView>(R.id.nav_name_surname)
        val email = headerView.findViewById<TextView>(R.id.nav_email)

        // After Authentication
        val user = Firebase.auth.currentUser
        user?.let {
            nameSurname.text = it.displayName
            email.text = it.email
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showAlertDialogLogout() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")

        // Logout
        builder.setPositiveButton("Logout") { dialog: DialogInterface, i: Int -> signOut() }

        // Do nothing
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, i: Int -> dialog.dismiss() }

        builder.show()
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
    }
}