package com.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ItemContentBinding
import com.example.bookshelf.network.models.BookShelfInfo
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContentAdapter(private var dataList: List<BookShelfInfo?>?,
                     private val onItemSelected: (bookShelfInfo: BookShelfInfo?) -> Unit) :
    RecyclerView.Adapter<ContentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemContentBinding =
            ItemContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = dataList?.get(holder.adapterPosition)

        if (model != null) {
            val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            var year : String? = null
            model.publishedChapterDate?.let {
                year = dateFormat.format(Date(it.times(1000))).toInt().toString()
            }
            Picasso.get()
                .load(model.image)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(holder.viewBinding.ivBook)

            holder.viewBinding.tvBookTitle.text = model.title
            holder.viewBinding.tvBookScore.text = model.score.toString()
            year?.let {
                holder.viewBinding.tvBookPublishedDate.isVisible = true
                holder.viewBinding.tvBookPublishedDate.text = holder.itemView.context.getString(R.string.publish_date,it)
            } ?:run {
                holder.viewBinding.tvBookPublishedDate.isVisible = false
            }

            holder.viewBinding.root.setOnClickListener {
                onItemSelected.invoke(model)
            }

        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    open class MyViewHolder(val viewBinding: ItemContentBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}