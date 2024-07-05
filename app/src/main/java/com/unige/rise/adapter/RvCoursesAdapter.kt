package com.unige.rise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unige.rise.databinding.RvCourseItemBinding
import com.unige.rise.models.Courses

class RvCoursesAdapter(private val courseList : java.util.ArrayList<Courses>) : RecyclerView.Adapter<RvCoursesAdapter.ViewHolder>() {

    class ViewHolder(val binding : RvCourseItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(RvCourseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = courseList[position]
        holder.apply {
            binding.apply {
                tvNameItem.text = currentItem.name
                tvPhoneItem.text = currentItem.phoneNumber
                tvIdItem.text = currentItem.id
            }
        }
    }

}