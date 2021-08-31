package com.asiasquare.byteg.shoppingdemo.payment.stripepayment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewPaymentItemListBinding
import com.asiasquare.byteg.shoppingdemo.util.round

class ItemListPaymentAdapter(private val onClickListener: OnClickListener) : ListAdapter<ShoppingBasketItem, ItemListPaymentAdapter.ItemListPaymentViewHolder>(DiffCallback){

    class ItemListPaymentViewHolder(private val binding: GridViewPaymentItemListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item : ShoppingBasketItem){
            binding.tvName.text = item.itemName
            binding.tvAmount.text = "Số lượng: ${item.itemAmount}"
            binding.ivPicture.load(item.itemImageSource)
            binding.tvPrice.text = item.itemPrice.round(2).toString()
        }

        companion object{
            fun from(parent: ViewGroup): ItemListPaymentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewPaymentItemListBinding.inflate(layoutInflater, parent, false)
                return ItemListPaymentViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListPaymentViewHolder {
        return ItemListPaymentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemListPaymentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(item)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShoppingBasketItem>(){
        override fun areItemsTheSame(oldItem: ShoppingBasketItem, newItem: ShoppingBasketItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ShoppingBasketItem, newItem: ShoppingBasketItem): Boolean {
            return oldItem == newItem
        }

    }

    class OnClickListener(val clickListener : (item: ShoppingBasketItem) -> Unit){
        fun onClick(item: ShoppingBasketItem) = clickListener(item)
    }
}

