package com.asiasquare.byteg.shoppingdemo.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.databinding.GirdViewCartItemBinding
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList

class CartFragmentAdapter(private val onClickListener: OnClickListener) : ListAdapter<ItemList, CartFragmentAdapter.CartViewHolder>(DiffCallback){

    class CartViewHolder(private val binding: GirdViewCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: ItemList) {
            binding.apply {
                binding.anhItemGioHang.load(cart.imgResource)
                tenItemGioHang.text = cart.textTenSanPham

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
        holder.itemView.setOnClickListener {

        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemList>(){
        override fun areItemsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem == newItem
        }

    }

    /** Simple ClickListener. Return cart Object info when user click **/
    class OnClickListener(val clickListener : (cart : ItemList) -> Unit){
        fun onClick(cart : ItemList) = clickListener(cart)
    }


}