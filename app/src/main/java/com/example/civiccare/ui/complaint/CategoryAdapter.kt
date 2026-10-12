package com.example.civiccare.ui.complaint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.civiccare.R
import com.example.civiccare.data.model.Category

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private var items: List<Category> = emptyList()

    fun submit(list: List<Category>) {
        items = list
        notifyDataSetChanged()
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.tvCategoryIcon)
        val name: TextView = view.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val category = items[position]
        holder.name.text = category.categoryName
        holder.icon.text = iconFor(category.categoryName)
        holder.itemView.setOnClickListener { onClick(category) }
    }

    override fun getItemCount() = items.size

    private fun iconFor(name: String): String = when {
        name.contains("Road") -> "🛣️"
        name.contains("Garbage") -> "🗑️"
        name.contains("Water") -> "💧"
        name.contains("Street") -> "💡"
        name.contains("Drainage") -> "🌊"
        name.contains("Electric") -> "⚡"
        name.contains("Safety") -> "🛡️"
        else -> "📌"
    }
}