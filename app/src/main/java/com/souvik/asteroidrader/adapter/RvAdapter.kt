package com.souvik.asteroidrader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.souvik.asteroidrader.R
import com.souvik.asteroidrader.databinding.RowLayoutBinding
import com.souvik.asteroidrader.model.ApiResponse

class RvAdapter(
    val list: ArrayList<ApiResponse>,
    val context: Context,
    val onClickViewListner: onClickView
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApiResponse) {
            binding.tvName.text = data.name
            binding.tvDate.text = data.date
            binding.ivPotential.setImageDrawable(context.getDrawable(if (data.isPotentiallyHazard) R.drawable.ic_face_sad else R.drawable.ic_face))
            binding.clConatiner.setOnClickListener {
                onClickViewListner.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.ViewHolder {
        val binding = DataBindingUtil.inflate<RowLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface onClickView {
        fun onClick(position: Int)
    }
}