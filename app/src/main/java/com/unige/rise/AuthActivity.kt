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
            btnLoginAnonymous.setOnClickListener { signInWith("anonymous") }
        }
    }

    private fun signInWith(signInMethod : String) {
        val providers = when (signInMethod) {
            "email" -> arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            "google" -> arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            "anonymous" -> arrayListOf(AuthUI.IdpConfig.AnonymousBuilder().build())
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
        //val response = result.idpResponse // TODO: remove this variable if not used
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser

            // Start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        } else {
            // Sign in failed.
            // If response is null the user canceled the sign-in flow using the back button.
            // Otherwise check response.getError().getErrorCode() and handle the error.

            // MODO 1:
            //throw errorInAuthentication("Autenticazione fallita")

            // MODO 2:
            /*
            Toast.makeText(this, "Autenticazione fallita", Toast.LENGTH_LONG).show()

            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            */

            // MODO 3:
            finish()
            startActivity(getIntent())
        }
    }

    // TODO: remove these lines of code
    /*
    private fun errorInAuthentication(e: String): AuthenticationException {
        Toast.makeText(this, e, Toast.LENGTH_LONG).show()

        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)

        return AuthenticationException(e)
    }
    */
}

//class AuthenticationException(message : String) : RuntimeException(message)