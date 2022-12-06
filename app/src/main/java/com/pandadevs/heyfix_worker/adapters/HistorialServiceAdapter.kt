package com.pandadevs.heyfix_worker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.HistorialServiceModel
import com.pandadevs.heyfix_worker.databinding.ItemHistoryServiceBinding

class HistorialServiceAdapter(context: Context) :
    ListAdapter<HistorialServiceModel, HistorialServiceAdapter.ViewHolder>(DiffUtilCallback) {

    val globalContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            ItemHistoryServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemHistoryServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistorialServiceModel, position: Int) = with(binding) {
            binding.tvName.text = item.client_name
            binding.tvAddress.text = item.address
            binding.tvDateHired.text = item.date_hired.toDate().toString()
            if(item.completed){
                binding.tvStatus.text = "Completado"
                binding.tvStatus.setTextColor(ContextCompat.getColor(globalContext, R.color.Accent))
            }
            if(item.canceled){
                binding.tvStatus.text = "Cancelado"
                binding.tvStatus.setTextColor(ContextCompat.getColor(globalContext, R.color.md_theme_light_tertiary))
            }
        }
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<HistorialServiceModel>() {
        override fun areItemsTheSame(
            oldItem: HistorialServiceModel,
            newItem: HistorialServiceModel
        ): Boolean =
            oldItem == newItem


        override fun areContentsTheSame(
            oldItem: HistorialServiceModel,
            newItem: HistorialServiceModel
        ): Boolean =
            newItem == oldItem

    }
}