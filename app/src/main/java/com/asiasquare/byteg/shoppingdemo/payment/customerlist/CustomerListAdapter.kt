package com.asiasquare.byteg.shoppingdemo.payment.customerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asiasquare.byteg.shoppingdemo.database.customers.Customer
import com.asiasquare.byteg.shoppingdemo.databinding.ListItemCustomerBinding


class CustomerListAdapter(private val onClickListener: OnClickListener) : ListAdapter<Customer, CustomerListAdapter.DomainCustomerViewHolder>(DiffCallback){

    class DomainCustomerViewHolder(private val binding: ListItemCustomerBinding): RecyclerView.ViewHolder(binding.root) {

        val btDelete = binding.btDelete
        fun bind(item : Customer){
            binding.tvName.text = item.name
            binding.tvAddress.text = item.address.toString()

        }

        companion object{
            fun from(parent: ViewGroup): DomainCustomerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCustomerBinding.inflate(layoutInflater, parent, false)
                return DomainCustomerViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainCustomerViewHolder {
        return DomainCustomerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DomainCustomerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(item)
        }
        holder.btDelete.setOnClickListener {
            onClickListener.onDeleteClick(item)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Customer>(){
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.customerId == newItem.customerId
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }

    }

    interface OnClickListener{
        fun onItemClick(customer: Customer)
        fun onDeleteClick(customer:Customer)
    }
}