package com.pandadevs.heyfix_worker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandadevs.heyfix_worker.databinding.ItemMemberBinding

class AdapterMember(private val list: List<Member>, private val context: Context) :
    RecyclerView.Adapter<AdapterMember.Holder>() {

    class Holder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ItemMemberBinding>(
            LayoutInflater.from(context),
            R.layout.item_member,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val member: Member = list[position]
        holder.binding.tvName.text = member.name
        holder.binding.tvRole.text = member.role
        holder.binding.ivImage.setImageDrawable(ContextCompat.getDrawable(context, member.image))
    }

    override fun getItemCount(): Int = list.size
}