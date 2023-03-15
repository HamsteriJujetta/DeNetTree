package com.example.denettree

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var currentItems: List<Node> = emptyList()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNodeName: TextView
        val ivDeleteNode: ImageView

        init {
            tvNodeName = view.findViewById(R.id.tvNodeName)
            ivDeleteNode = view.findViewById(R.id.ivDeleteNode)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tree_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvNodeName.text = currentItems[position].name
        //Log.d("Hamster", "onBindViewHolder name = ${currentItems[position].name}")

        viewHolder.itemView.setOnClickListener {
            onItemClick(position)
        }

        viewHolder.ivDeleteNode.setOnClickListener {
            onItemDelete(position)
        }
    }

    override fun getItemCount() = currentItems.size

    fun setCurrentItems(items: List<Node>) {
        currentItems = items
        notifyDataSetChanged()
    }

}