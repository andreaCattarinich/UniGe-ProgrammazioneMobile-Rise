package com.unige.rise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import com.unige.rise.databinding.RvCourseItemBinding
import com.unige.rise.models.Courses

class RvCoursesAdapter(private val courseList : java.util.ArrayList<Courses>) : RecyclerView.Adapter<RvCoursesAdapter.ViewHolder>() {
    // for OnClick RecyclerView items
    private lateinit var mListener: OnItemClickListener

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
            binding.apply {
                tvTitleItem.text = currentItem.title
                tvSubtitleItem.text = currentItem.subtitle
                //tvCourseCompletion.text =
                val db : FirebaseFirestore = Firebase.firestore
                val user = Firebase.auth.currentUser
                if (user != null) {
                    db.collection(user.uid)
                        .document("course${position+1}")
                        .get()
                        .addOnSuccessListener { data ->
                            val courseCompletion = data.get("score").toString().toInt()

                            when (courseCompletion) {
                                in 0..30 -> tvCourseCompletion.setTextColor(android.graphics.Color.RED)
                                in 31..70 -> tvCourseCompletion.setTextColor(android.graphics.Color.YELLOW)
                                else -> tvCourseCompletion.setTextColor(android.graphics.Color.GREEN)
                            }
                            tvCourseCompletion.text = "${courseCompletion}%"

                            Picasso.get().load(currentItem.imgUrl).into(imgItem)
                        }
                }
            }
        }
    }

}