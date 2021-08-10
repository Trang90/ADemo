package com.asiasquare.byteg.shoppingdemo.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.databinding.GirdViewCartItemBinding

class CartFragmentAdapter(private val onClickListener: OnClickListener) : ListAdapter<ShoppingBasketItem, CartFragmentAdapter.CartViewHolder>(DiffCallback){

    class CartViewHolder(private val binding: GirdViewCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val btDelete = binding.buttonXoaGioHang
        val btAdd = binding.buttonTang
        val btMinus = binding.buttonGiam

        @SuppressLint("SetTextI18n")
        fun bind(cart: ShoppingBasketItem) {
            val amount = cart.itemAmount
            binding.apply {
                binding.anhItemGioHang.load(cart.itemImageSource)
                tenItemGioHang.text = cart.itemName
                giaItemGioHang.text = "€" + cart.totalPrice.toString()
                khoiLuongItemGioHang.text = "Khối lượng: "+ cart.itemWeight
                tvItemAmount.text = cart.itemAmount.toString()
                when {
                    amount < 2 -> {
                        buttonGiam.visibility = View.GONE
                        buttonTang.visibility = View.VISIBLE
                    }
                    amount < 50 -> {
                        buttonGiam.visibility = View.VISIBLE
                        buttonTang.visibility = View.VISIBLE
                    }
                    else -> {
                        buttonGiam.visibility = View.VISIBLE
                        buttonTang.visibility = View.GONE
                    }
                }
            }
        }



        /** inflate the small item in recyclerView **/
        companion object{
            fun from(parent: ViewGroup): CartViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GirdViewCartItemBinding.inflate(layoutInflater, parent, false)
                return CartViewHolder(binding)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.btDelete.setOnClickListener {
            onClickListener.onDeleteClick(item)
        }
        holder.btAdd.setOnClickListener {
            onClickListener.onAddClick(item)
        }
        holder.btMinus.setOnClickListener {
            onClickListener.onMinusClick(item)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ShoppingBasketItem>(){
        override fun areItemsTheSame(oldItem: ShoppingBasketItem, newItem: ShoppingBasketItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ShoppingBasketItem, newItem: ShoppingBasketItem): Boolean {
            return oldItem == newItem
        }

    }

    /** Simple ClickListener. Return cart Object info when user click **/
    interface OnClickListener{
        fun onDeleteClick(cart: ShoppingBasketItem)
        fun onAddClick(cart: ShoppingBasketItem)
        fun onMinusClick(cart: ShoppingBasketItem)
    }


}