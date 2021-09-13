package com.asiasquare.byteg.shoppingdemo.search

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewSearchItemBinding

class SearchFragmentAdapter(private val onClickListener: OnClickListener) : ListAdapter<LocalItem, SearchFragmentAdapter.SearchViewHolder>(DiffCallback){

    class SearchViewHolder(private val binding:GridViewSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val btnFavorite= binding.imageViewAddFavorite
        @SuppressLint("SetTextI18n")
        fun bind(item: LocalItem) {

            val priceDiscounted = item.itemDiscountedPrice

            binding.apply {
                anhsanpham.load(item.itemImageSource)
                tensanpham.text = item.itemName

                if (priceDiscounted != 0.0) {
                    giasanpham.text = "€" + item.itemPrice.toString()
                    giasanpham.paintFlags = giasanpham.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvDiscountedPrice.text = "€${item.itemDiscountedPrice}"
                    binding.tvDiscountedPrice.visibility = View.VISIBLE
                } else {
                    binding.tvDiscountedPrice.visibility = View.INVISIBLE
                    giasanpham.text = "€" + item.itemPrice.toString()
                    giasanpham.paintFlags = giasanpham.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }



        /** inflate the small item in recyclerView **/
        companion object{
            fun from(parent: ViewGroup): SearchViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewSearchItemBinding.inflate(layoutInflater, parent, false)
                return SearchViewHolder(binding)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
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
        }

    }

    /** Simple ClickListener. Return cart Object info when user click **/
    interface OnClickListener {
        fun onItemClick(item: LocalItem)
        fun onAddFavoriteClick(item: LocalItem)

    }


}