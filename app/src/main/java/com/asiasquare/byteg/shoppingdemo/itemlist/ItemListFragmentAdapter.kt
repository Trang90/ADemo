package com.asiasquare.byteg.shoppingdemo.itemlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewItemListBinding
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList


class ItemListFragmentAdapter(private val onClickListener: OnClickListener):ListAdapter <ItemList, ItemListFragmentAdapter.ItemListViewHolder>(DiffCallback) {


    class ItemListViewHolder (private val binding: GridViewItemListBinding):
        RecyclerView.ViewHolder(binding.root)  {

        fun bind(item: ItemList) {
            binding.apply {
                binding.anhsanpham.load(item.imgResource)
                tensanpham.text = item.textTenSanPham
               // ItemList = item
//          giaItemGioHang.text = item.textGiaSanPham
//          giakhuyenmai.text = item.textGiaKhuyenMai
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

    companion object DiffCallback : DiffUtil.ItemCallback<ItemList>(){
        override fun areItemsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemList, newItem: ItemList): Boolean {
            return oldItem == newItem
        }

    }

    /** Simple ClickListener. Return itemList Object info when user click **/
    class OnClickListener(val clickListener : (itemList : ItemList) -> Unit){
        fun onClick(itemList : ItemList) = clickListener(itemList)
    }

}

