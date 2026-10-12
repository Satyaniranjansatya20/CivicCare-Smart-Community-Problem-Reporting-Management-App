package com.example.civiccare.ui.complaint

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.civiccare.R
import com.example.civiccare.data.model.Complaint

class ComplaintAdapter(
    private val onClick: (Complaint) -> Unit
) : RecyclerView.Adapter<ComplaintAdapter.VH>() {

    private var items: List<Complaint> = emptyList()

    fun submit(list: List<Complaint>) {
        items = list
        notifyDataSetChanged()
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.tvIcon)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val meta: TextView = view.findViewById(R.id.tvMeta)
        val date: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_complaint, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.icon.text = iconFor(c.category)
        holder.title.text = c.title
        holder.status.text = c.status
        holder.status.setTextColor(
            when (c.status) {
                "Resolved" -> Color.parseColor("#2E7D32")
                "In Progress" -> Color.parseColor("#1565C0")
                else -> Color.parseColor("#EF6C00")
            }
        )
        holder.meta.text = "${c.category} • ${c.location}"
        holder.date.text = "CC${1000 + c.complaintId}  •  ${c.date}"
        holder.itemView.setOnClickListener { onClick(c) }
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