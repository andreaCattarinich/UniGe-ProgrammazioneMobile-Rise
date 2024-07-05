package com.unige.rise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.unige.rise.adapter.RvCoursesAdapter
import com.unige.rise.databinding.FragmentHomeBinding
import com.unige.rise.models.Courses
import androidx.recyclerview.widget.LinearLayoutManager


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    // Recycler View
    private lateinit var coursesList : ArrayList<Courses>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Recycler View
        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        coursesList = arrayListOf()

        fetchData()

        binding.rvCourses.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }

        /*
        // Set user information in the fragment
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName?.split(" ")?.first()
            if (name != null) {
                binding.homeWelcome.text = "Welcome ${name}"
            }
        }

        // Get instances
        db = com.google.firebase.Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()
        */

        return root
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
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, " error : $error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = view.findViewById<LinearLayout>(R.id.img_course1)

        // Create a storage reference from my app
        val storageReference = FirebaseStorage.getInstance().reference

        // Create a reference with an initial file path and name
        val pathReference = storageReference.child("inflazione.png")

        val ONE_MEGABYTE: Long = 1024 * 1024
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->

            // Data for "images/island.jpg" is returned, use this as needed

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val drawable = BitmapDrawable(resources, bitmap)
            linearLayout.background = drawable
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(requireContext(), "Errore...", Toast.LENGTH_SHORT).show()

        }


    }
    */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
