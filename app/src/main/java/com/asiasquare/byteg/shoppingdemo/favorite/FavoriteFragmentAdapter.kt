package com.asiasquare.byteg.shoppingdemo.favorite

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewFavoriteItemBinding
import java.util.*


class FavoriteFragmentAdapter (private val onClickListener: OnClickListener): ListAdapter<FavoriteItem, FavoriteFragmentAdapter.FavoriteViewHolder>(DiffCallback) {


    /** ViewHolder class **/
    class FavoriteViewHolder(private val binding: GridViewFavoriteItemBinding):RecyclerView.ViewHolder(binding.root) {
        /** Bind item to View, load image here using Coil */

        val btDelete = binding.buttonXoaYeuThich
        val btAddToCart = binding.buttonThem

        @SuppressLint("SetTextI18n")
        fun bind (favorite: FavoriteItem) {
            val priceDiscounted = favorite.itemDiscountedPrice
            binding.apply {
                anhItemYeuThich.load(favorite.itemImageSource)
                tenItemYeuThich.text = favorite.itemName
                khoiLuongItemYeuThich.text = favorite.itemWeight

                if (priceDiscounted != 0.0) {
                    giaItemYeuThich.text = "€" + favorite.itemPrice.toString()
                    giaItemYeuThich.paintFlags = giaItemYeuThich.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvDiscountedPrice.text = "€${favorite.itemDiscountedPrice}"
                    binding.tvDiscountedPrice.visibility = View.VISIBLE
                } else {
                    binding.tvDiscountedPrice.visibility = View.INVISIBLE
                    giaItemYeuThich.text = "€" + favorite.itemPrice.toString()
                    giaItemYeuThich.paintFlags = giaItemYeuThich.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        /** inflate the small item in recyclerView **/
        companion object{
            fun from (parent: ViewGroup): FavoriteViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewFavoriteItemBinding.inflate(layoutInflater,parent,false)
                return FavoriteViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(item)
        }

        holder.btDelete.setOnClickListener {
            onClickListener.onDeleteClick(item)
        }

        holder.btAddToCart.setOnClickListener {
            onClickListener.addToCard(item)
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<FavoriteItem>(){
        override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean {
            return oldItem == newItem
        }


    }

    /** Simple ClickListener. Return favorite Object info when user click **/
    interface OnClickListener{
        fun onItemClick(favorite: FavoriteItem)
        fun onDeleteClick(favorite: FavoriteItem)
        fun addToCard (favorite: FavoriteItem)
    }


}