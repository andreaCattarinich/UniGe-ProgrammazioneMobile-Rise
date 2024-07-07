package com.unige.rise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unige.rise.databinding.RvCourseItemBinding
import com.unige.rise.models.Courses


// TODO:
//class RvCoursesAdapter(private val courseList : ArrayList<Courses>) : RecyclerView.Adapter<RvCoursesAdapter.ViewHolder>() {
class RvCoursesAdapter(private val courseList : java.util.ArrayList<Courses>) : RecyclerView.Adapter<RvCoursesAdapter.ViewHolder>() {

    // for OnClick RecyclerView items
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }

    class ViewHolder(val binding : RvCourseItemBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
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
                //tvIdItem.text = currentItem.id

                Picasso.get().load(currentItem.imgUrl).into(imgItem)

                /* Come cambiare activity dopo aver cliccato sopra un item
                holder.itemView.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        //val activity = v!!.context as AppCompatActivity
                        //activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_home, QuizFragment()).addToBackStack(null).commit()

                    }
                })
                */
            }
        }
    }

}