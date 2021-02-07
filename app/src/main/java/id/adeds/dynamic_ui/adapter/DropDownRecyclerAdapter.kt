package id.adeds.dynamic_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DropDownValue
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.item_filter.view.*

class DropDownRecyclerAdapter(private val data: List<DropDownValue>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataShow = data

    fun showDataFilter(value: List<DropDownValue>) {
        dataShow = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataShow.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        val viewHolder =  PartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_filter,
                parent,
                false
            ))
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val selected = dataShow[position]
            if (selected.isSelected) selected.isSelected = false
            else {
                data.forEach { it.isSelected = false }
                data.find { it == selected }?.isSelected = true
            }
            notifyDataSetChanged()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(dataShow[position])
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: DropDownValue) {
            itemView.textName.text = part.value
            setView(part.isSelected)
        }
        private fun setView(isSelected: Boolean) {
            val linearColor = if (isSelected) R.color.ice_blue else R.color.white
            val textColor = if (isSelected) R.color.blue else R.color.gun_metal
            itemView.imageCheck.visible = isSelected
            itemView.linearItem.setBackgroundColor(ContextCompat.getColor(itemView.context, linearColor))
            itemView.textName.setTextColor(ContextCompat.getColor(itemView.context, textColor))
        }
    }
}