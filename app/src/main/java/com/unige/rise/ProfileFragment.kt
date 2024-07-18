package com.unige.rise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.unige.rise.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    // Databinding and ViewModel
    private lateinit var binding : FragmentProfileBinding
    private val viewModel : ProfileViewModel by viewModels()

    private lateinit var db : FirebaseFirestore
    private lateinit var realtimeDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser

        if(user == null || user.isAnonymous){
            Toast.makeText(requireContext(), "Please sign-in!", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        } else {
            viewModel.updateDisplayName(user.displayName.toString())
            viewModel.updateEmail(user.email.toString())

            db = Firebase.firestore
            db.collection(user.uid)
                .get()
                .addOnSuccessListener { result ->
                    var totalScore = 0
                    for (course in result) {
                        //Log.d("RiseLOG", "${course.id} => ${course.data}")
                        totalScore += (course.get("score") as Long).toInt()
                    }

                    realtimeDB = FirebaseDatabase.getInstance().reference
                    realtimeDB.child("courses")
                        .get()
                        .addOnSuccessListener { data ->
                            //Log.d("RiseLOG", "N. corsi(2) = > ${data.childrenCount}")
                            val completion = (totalScore/data.childrenCount).toInt()

                            when (completion) {
                                in 31..70 -> binding.courseCompletion.setTextColor(android.graphics.Color.YELLOW)
                                in 71..100 -> binding.courseCompletion.setTextColor(android.graphics.Color.GREEN)
                                else -> binding.courseCompletion.setTextColor(android.graphics.Color.RED)
                            }

                            viewModel.updateCourseCompletion(completion)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.d("RiseLOG", "Error getting documents: ", exception)
                }
        }
    }
}