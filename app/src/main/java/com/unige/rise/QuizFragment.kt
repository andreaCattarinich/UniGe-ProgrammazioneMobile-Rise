package com.unige.rise

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class QuizFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get instances
        db = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageView = view.findViewById<ImageView>(R.id.firstCourse)

        // Create a storage reference from my app
        val storageReference = FirebaseStorage.getInstance().reference

        // Create a reference with an initial file path and name
        val pathReference = storageReference.child("inflazione.png")

        val ONE_MEGABYTE: Long = 1024 * 1024
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->

            // Data for "images/island.jpg" is returned, use this as needed

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageView.setImageBitmap(bitmap)

            Toast.makeText(requireContext(), "Immagine ok...", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(requireContext(), "Errore...", Toast.LENGTH_SHORT).show()

        }
    }


}