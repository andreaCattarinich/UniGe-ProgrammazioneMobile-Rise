package com.unige.rise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.unige.rise.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    // Finalmente ho fatto il merge
    private lateinit var binding : ActivityAuthBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        binding.apply {
            btnLoginEmail.setOnClickListener { signInWith("email") }
            btnLoginGoogle.setOnClickListener { signInWith("google") }
            // btnLoginAnonymous.setOnClickListener { signInWith("anonymous") }
        }
    }

    private fun signInWith(signInMethod : String) {
        val providers = when (signInMethod) {
            "email" -> arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            "google" -> arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            //"anonymous" -> arrayListOf(AuthUI.IdpConfig.AnonymousBuilder().build())
            else -> throw IllegalArgumentException("Illegal sign-in method: ($signInMethod)")
        }
        startFirebaseUIAuth(providers)
    }

    private fun startFirebaseUIAuth(providers: List<AuthUI.IdpConfig>) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser

            // Start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        } else {
            startActivity(intent)
            finish()
        }
    }
}