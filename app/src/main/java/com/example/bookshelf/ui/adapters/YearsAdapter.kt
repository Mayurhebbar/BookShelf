package com.example.bookshelf.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ItemYearsBinding
import com.example.bookshelf.network.models.YearInfo

class YearsAdapter(
    private val mContext: Context,
    private val years: MutableList<YearInfo>,
    val onItemSelected: (selectedItemIndex: Int) -> Unit
) : RecyclerView.Adapter<YearsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: ItemYearsBinding =
            ItemYearsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val yearInfo = years[holder.adapterPosition]

        holder.viewBinding.tvYear.text = yearInfo.year

        if (yearInfo.isSelected) {
            holder.viewBinding.tvYear.setTextColor(
                ContextCompat.getColor(mContext, R.color.white)
            )
            holder.viewBinding.tvYear.background =
                ContextCompat.getDrawable(mContext, R.drawable.unslected_button)
        } else {
            holder.viewBinding.tvYear.setTextColor(
                ContextCompat.getColor(mContext, R.color.black)
            )
            holder.viewBinding.tvYear.background =
                ContextCompat.getDrawable(mContext, R.drawable.selected_button)
        }

        holder.viewBinding.tvYear.setOnClickListener {
            onItemSelected(holder.adapterPosition)
        }
    }


    override fun getItemCount(): Int {
        return years.size
    }

    fun updateList(position: Int) {
        for ((index, model) in years.withIndex()) {
            model.isSelected = position == index
        }
        notifyDataSetChanged()
    }

    open class MyViewHolder(val viewBinding: ItemYearsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}