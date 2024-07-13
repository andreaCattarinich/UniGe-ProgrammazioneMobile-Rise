package com.unige.rise

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.unige.rise.databinding.ActivityMainBinding
import com.unige.rise.databinding.NavHeaderMainBinding

class MainActivity : AppCompatActivity() {

    // Databinding and ViewModel
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        // Get reference to nav_header_main
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderBinding: NavHeaderMainBinding = DataBindingUtil.bind(headerView)!!
        navHeaderBinding.viewModel = viewModel

        // Remove title in Toolbar
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup Navigation Drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile
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

        viewModel.setupNavigationDrawerData()


        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        // FCM
        //Require the permission POST_NOTIFICATIONS
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMExample", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("FCMExample", token)
            //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
                true
            }   // Se l'item selezionato appartiene alla navigationDrawer gestiscilo con la classe padre
            else -> super.onOptionsItemSelected(item)
        }
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
        auth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}