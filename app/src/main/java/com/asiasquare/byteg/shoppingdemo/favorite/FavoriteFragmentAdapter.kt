package com.asiasquare.byteg.shoppingdemo.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewFavoriteItemBinding
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList


class FavoriteFragmentAdapter (private val onClickListener: OnClickListener): ListAdapter<ItemList, FavoriteFragmentAdapter.FavoriteViewHolder>(DiffCallback) {

    /** ViewHolder class **/
    class FavoriteViewHolder(private val binding: GridViewFavoriteItemBinding):RecyclerView.ViewHolder(binding.root) {
        /** Bind item to View, load image here using Coil */
        fun bind (favorite: ItemList){
            binding.anhItemYeuThich.load(favorite.imgResource)
            binding.tenItemYeuThich.text = favorite.textTenSanPham
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
            onClickListener.clickListener(item)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ItemList>(){
        override fun areItemsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem == newItem
        }


    }

    /** Simple ClickListener. Return favorite Object info when user click **/
    class OnClickListener(val clickListener : (favorite : ItemList) -> Unit){
        fun onClick(favorite: ItemList) = clickListener(favorite)
    }


}