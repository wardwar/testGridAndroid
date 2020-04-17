package app.by.wildan.testgridandroid.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.by.wildan.testgridandroid.R
import app.by.wildan.testgridandroid.data.entity.ImageContent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*

class ContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemData: MutableList<ImageContent> = mutableListOf()

    fun notifyDataSetChanged(addingItems: List<ImageContent>) {
        with(itemData) {
            clear()
            addAll(addingItems)
            notifyDataSetChanged()
        }
    }

    fun appendData(addingItems: List<ImageContent>) {
        with(itemData) {
            addAll(addingItems)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DefaultView) {
            with(holder) {
                bind(itemData[position])
            }
        }
    }

    inner class DefaultView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ImageContent) = with(itemView) {
            Picasso.get().load(item.urls.small).into(imgContent)
        }
    }


}