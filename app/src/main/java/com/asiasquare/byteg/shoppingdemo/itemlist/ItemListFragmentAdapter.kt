package com.asiasquare.byteg.shoppingdemo.itemlist

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewItemListBinding
import com.asiasquare.byteg.shoppingdemo.util.round


class ItemListFragmentAdapter(private val onClickListener: OnClickListener):ListAdapter <LocalItem, ItemListFragmentAdapter.ItemListViewHolder>(DiffCallback) {

    class ItemListViewHolder (private val binding: GridViewItemListBinding):
        RecyclerView.ViewHolder(binding.root)  {

        val btnFavorite= binding.ivAddFavorite
        @SuppressLint("SetTextI18n")
        fun bind(item: LocalItem) {

            val priceDiscounted = item.itemDiscountedPrice

            binding.apply {
                anhsanpham.load(item.itemImageSource){
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_connection_error)
                }
                tensanpham.text = item.itemName

                if (priceDiscounted != 0.0) {

                    giasanpham.text= "€" + item.itemPrice.toString()
                    giasanpham.paintFlags = giasanpham.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvDiscountedPrice.text= "€${item.itemDiscountedPrice}"
                    binding.tvDiscountedPrice.visibility = View.VISIBLE
                } else {
                    binding.tvDiscountedPrice.visibility = View.INVISIBLE
                    giasanpham.text= "€" + item.itemPrice.toString()
                    giasanpham.paintFlags = giasanpham.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

//                when {
//                    item.itemFavorite -> ivAddFavorite.setImageResource(R.drawable.timdo24)
//                    else -> ivAddFavorite.setImageResource(R.drawable.timden24)
//                }
            }
        }
        companion object{
            fun from(parent: ViewGroup): ItemListViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewItemListBinding
                    .inflate(layoutInflater, parent, false)
                return ItemListViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ItemListViewHolder {
        return ItemListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder,position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(item)
        }

        holder.btnFavorite.setOnClickListener {
            onClickListener.onAddFavoriteClick(item)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LocalItem>(){
        override fun areItemsTheSame(oldItem: LocalItem, newItem: LocalItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: LocalItem, newItem: LocalItem): Boolean {
            return oldItem == newItem
//            return false
        }

    }

    /** Simple ClickListener. Return itemList Object info when user click **/

    interface OnClickListener {
        fun onItemClick(item: LocalItem)
        fun onAddFavoriteClick(item: LocalItem)

    }
}
