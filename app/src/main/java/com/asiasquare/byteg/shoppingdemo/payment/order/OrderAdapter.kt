package com.asiasquare.byteg.shoppingdemo.payment.order

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewOrderItemBinding
import com.asiasquare.byteg.shoppingdemo.util.round


class OrderAdapter(private val onClickListener: OnClickListener) : ListAdapter<ShoppingBasketItem, OrderAdapter.OrderViewHolder>(DiffCallback){

    class OrderViewHolder(private val binding: GridViewOrderItemBinding): RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(item : ShoppingBasketItem){
            val itemPrice = item.itemPrice
            val itemAmount = item.itemAmount
            val priceItems = (itemPrice * itemAmount).round(2)
            val priceDiscounted = (item.itemDiscountedPrice * item.itemAmount).round(2)

            binding.tvName.text = item.itemName
            binding.tvAmount.text = "${item.itemAmount}x"
            binding.ivPicture.load(item.itemImageSource)
            binding.tvWeight.text = item.itemWeight
            binding.tvPriceOfItem.text =  "€${priceItems}"


            if (priceDiscounted != 0.0) {

                binding.tvPrice.text= "€" + item.itemPrice.round(2).toString()
                binding.tvPrice.paintFlags = binding.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvDiscountedPrice.text= "€${item.itemDiscountedPrice}"
                binding.tvDiscountedPrice.visibility = View.VISIBLE
                binding.tvPriceOfItem.text= "€${priceDiscounted}"
            } else {
                binding.tvDiscountedPrice.visibility = View.INVISIBLE
                binding.tvPrice.text = "€" + item.itemPrice.round(2).toString()
                binding.tvPrice.paintFlags = binding.tvPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvPriceOfItem.text= "€${priceItems}"
            }
        }

        companion object{
            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewOrderItemBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(item)
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

//    class OnClickListener(val clickListener : (item: ShoppingBasketItem) -> Unit){
//        fun onClick(item: ShoppingBasketItem) = clickListener(item)
//    }

    interface OnClickListener{
        fun onItemClick(cart: ShoppingBasketItem)
    }

}