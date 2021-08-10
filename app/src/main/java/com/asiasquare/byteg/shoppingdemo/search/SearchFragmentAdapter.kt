package com.asiasquare.byteg.shoppingdemo.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
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
        fun bind(search: LocalItem) {
            binding.apply {
                anhsanpham.load(search.itemImageSource)
                tensanpham.text = search.itemName
                giasanpham.text = search.itemPrice.toString()
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