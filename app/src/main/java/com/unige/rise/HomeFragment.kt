package com.unige.rise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {

    // TODO: control if I need Safe Args
    //val args : DataFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val welcome = view.findViewById<TextView>(R.id.home_welcome)
        //val name = getIntent().getExtras().getString("USER")

        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName?.split(" ")
            if (name != null) {
                welcome.text = "Welcome ${name.first()}"
            }
        }

        return view
    }
}