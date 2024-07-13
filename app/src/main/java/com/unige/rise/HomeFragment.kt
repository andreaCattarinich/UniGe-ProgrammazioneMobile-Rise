package com.unige.rise

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unige.rise.adapter.RvCoursesAdapter
import com.unige.rise.databinding.FragmentHomeBinding
import com.unige.rise.models.Courses
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {

    // Databinding and ViewModel
    private lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    // Sign-in
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    // Recycler View
    private lateinit var coursesList : ArrayList<Courses>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val user = Firebase.auth.currentUser

        if (user != null) {
            if(user.isAnonymous) {
                viewModel.updateWelcomeText("Anon")
            } else {
                val name = user.displayName?.split(" ")?.first().toString()
                viewModel.updateWelcomeText(name)
            }
        } else {
            Toast.makeText(context, "User not logged", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        firebaseRef = FirebaseDatabase.getInstance().getReference("courses")
        coursesList = arrayListOf()

        fetchData()

        binding.rvCourses.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                coursesList.clear()
                if (snapshot.exists()) {
                    for (courseSnap in snapshot.children) {
                        val courses = courseSnap.getValue(Courses::class.java)
                        coursesList.add(courses!!)
                    }
                }

                val rvAdapter = RvCoursesAdapter(coursesList)
                binding.rvCourses.adapter = rvAdapter

                rvAdapter.setOnItemClickListener(object : RvCoursesAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        //Toast.makeText(requireContext(), "Clicked on $position", Toast.LENGTH_SHORT).show()

                        val intent = Intent(requireContext(), CourseActivity::class.java)
                        intent.putExtra("id", coursesList[position].id)
                        startActivity(intent)
                    }

                })

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, " error : $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}