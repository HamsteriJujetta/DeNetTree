package com.example.denettree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var currentItems: List<Node> = emptyList()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView

        init {
            tvName = view.findViewById(R.id.tvNodeName)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tree_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvName.text = currentItems[position].name

        viewHolder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = currentItems.size

    fun setCurrentItems(items: List<Node>) {
        currentItems = items
    }

}