package app.by.wildan.testgridandroid.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.by.wildan.testgridandroid.R
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemData: MutableList<String> = mutableListOf()
    private var selectedPosition = -1
    private var listener: OnItemClickListener? = null

    fun addOnItemClickListener(contiuation: (position: Int, category: String) -> Unit) {
        this.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int, category: String) {
                contiuation(position, category)
            }
        }
    }

    fun notifyDataSetChanged(addingItems: List<String>) {
        with(itemData) {
            clear()
            addAll(addingItems)
            selectedPosition = 0
            notifyDataSetChanged()
        }
    }

    fun selectItem(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun getCurrentSelectedPostiion(): String {
        return if (itemData.isNotEmpty())
            itemData[selectedPosition]
        else {
            ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DefaultView) {
            with(holder) {
                bind(itemData[position], position)
            }
        }
    }

    inner class DefaultView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, position: Int) = with(itemView) {
            textName.text = item

            val cardView = this as CardView
            if (selectedPosition == position) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.niceBlue))
                textName.setTextColor(ContextCompat.getColor(context, R.color.strongBlue))
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightGray))
                textName.setTextColor(ContextCompat.getColor(context, R.color.disableColor))
            }

            setOnClickListener {
                selectItem(position)
                listener?.onItemClick(position, item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, category: String)
    }

}