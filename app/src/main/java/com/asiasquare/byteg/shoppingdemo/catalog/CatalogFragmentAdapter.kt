package com.asiasquare.byteg.shoppingdemo.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewCatalogItemBinding
import com.asiasquare.byteg.shoppingdemo.datamodel.Catalog
class CatalogFragmentAdapter(private val onClickListener: OnClickListener) : ListAdapter<Catalog,CatalogFragmentAdapter.CatalogViewHolder>(DiffCallback){

    /** ViewHolder class **/
    class CatalogViewHolder(private val binding: GridViewCatalogItemBinding) : RecyclerView.ViewHolder(binding.root){

        /** Bind item to View, load image here using Coil */
        fun bind(item: Catalog){
            binding.ivCatalogGrid.load(item.imgResource)
            binding.tvCatalog.text = item.name
        }

        /** inflate the small item in recyclerView **/
        companion object{
            fun from(parent: ViewGroup): CatalogViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewCatalogItemBinding.inflate(layoutInflater, parent, false)
                return CatalogViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(item)
        }
    }


    /**
     * Check if items are changed. If yes will update the list using submitList (in Fragment)
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Catalog>(){
        override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
            return oldItem == newItem
        }

    }

    /** Simple clicklistener. Return catalog Object info when user click **/
    class OnClickListener(val clickListener : (catalog : Catalog) -> Unit){
        fun onClick(catalog: Catalog) = clickListener(catalog)
    }



}