package com.unige.rise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import com.unige.rise.databinding.RvCourseItemBinding
import com.unige.rise.models.Courses

class RvCoursesAdapter(private val courseList : java.util.ArrayList<Courses>) : RecyclerView.Adapter<RvCoursesAdapter.ViewHolder>() {

    // for OnClick RecyclerView items
    private lateinit var mListener: OnItemClickListener

    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    interface OnItemClickListener {
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding : RvCourseItemBinding, listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvCourseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false), mListener)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = courseList[position]
        holder.apply {
            binding.tvTitleItem.text = currentItem.title
            binding.tvSubtitleItem.text = currentItem.subtitle
            Picasso.get().load(currentItem.imgUrl).into(binding.imgItem)

            // Get from Firebase Database user's scores
            auth = FirebaseAuth.getInstance()

            val user = auth.currentUser
            db = Firebase.firestore

            if(user!!.isAnonymous) {
                return
            }

            db.collection(user.uid)
                .document("course${position + 1}")
                .get()
                .addOnSuccessListener { document ->
                    var currentScore = 0
                    val score = document.data?.get("score")

                    if(score != null) {
                        currentScore = (score as Long).toInt()
                    }

                    when (currentScore) {
                        in 31..70 -> binding.tvCourseCompletion.setTextColor(android.graphics.Color.YELLOW)
                        in 71..100 -> binding.tvCourseCompletion.setTextColor(android.graphics.Color.GREEN)
                        else -> binding.tvCourseCompletion.setTextColor(android.graphics.Color.RED)
                    }

                    binding.tvCourseCompletion.text = "${currentScore}%"
                }
        }
    }
}

// Firebase storage into RecyclerView references:
// Github: https://github.com/MohsenMashkour/FirebaseRealtimeExample
// YouTube: https://www.youtube.com/watch?v=_eTZowmape8&t=1s