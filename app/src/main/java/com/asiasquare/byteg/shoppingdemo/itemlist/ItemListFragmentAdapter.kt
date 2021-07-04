package com.asiasquare.byteg.shoppingdemo.itemlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewItemListBinding
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList


class ItemListFragmentAdapter(private val onClickListener: OnClickListener):ListAdapter <NetworkItem, ItemListFragmentAdapter.ItemListViewHolder>(DiffCallback) {


    class ItemListViewHolder (private val binding: GridViewItemListBinding):
        RecyclerView.ViewHolder(binding.root)  {

        fun bind(item: NetworkItem) {
            binding.apply {
                binding.anhsanpham.load(item.itemImageSource)
                tensanpham.text = item.itemName
                giasanpham.text= item.itemPrice.toString()
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
        val itemList = getItem(position)
        holder.bind(itemList)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(itemList)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NetworkItem>(){
        override fun areItemsTheSame(oldItem: NetworkItem, newItem: NetworkItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: NetworkItem, newItem: NetworkItem): Boolean {
            return oldItem == newItem
        }

    }

    /** Simple ClickListener. Return itemList Object info when user click **/
    class OnClickListener(val clickListener : (itemList : NetworkItem) -> Unit){
        fun onClick(itemList : NetworkItem) = clickListener(itemList)
    }

}


