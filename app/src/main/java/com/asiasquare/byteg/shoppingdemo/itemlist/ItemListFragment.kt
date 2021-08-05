package com.asiasquare.byteg.shoppingdemo.itemlist


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentItemListBinding
import com.asiasquare.byteg.shoppingdemo.itemlist.ListStatus.*
import kotlin.properties.Delegates
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem as NetworkItem

class ItemListFragment : Fragment(), ItemListFragmentAdapter.OnClickListener {

    private val args: ItemListFragmentArgs by navArgs()
    private var itemListCatalogId by Delegates.notNull<Int>()


    private var _binding : FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ItemListFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemListBinding.inflate(inflater,container,false)


        itemListCatalogId = args.catalogId

        val activity = requireNotNull(this.activity)
        viewModel = ViewModelProvider(this, ItemListFragmentViewModel.Factory(activity.application,itemListCatalogId))
            .get(ItemListFragmentViewModel::class.java)


        /** Create recyclerView adapter and define OnClickListener **/
        val adapter = ItemListFragmentAdapter(this)

        binding.recyclerViewCatalog.adapter = adapter

        /** ERROR, LOADING Event **/
        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (viewModel.status.value) {
                    ListStatus.LOADING -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.loading_animation)
                    }
                    ListStatus.ERROR -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    }
                    ListStatus.DONE -> {
                        binding.statusImage.visibility = View.GONE
                        binding.progressBar.visibility= View.GONE
                    }
                }
            }
        })
        /** Update data to adapter **/
        viewModel.list.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> Toast.makeText(context, "Đã thêm sản phẩm vào danh sách Yêu thích", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "Đã xóa sản phẩm khỏi danh sách Yêu thích", Toast.LENGTH_LONG).show()
            }
        })

        /** Navigate to detail by Id **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    ItemListFragmentDirections.actionItemListFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
            }
        })
        return binding.root
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: NetworkItem) {
        viewModel.onDetailClick(item)
    }

    override fun onAddFavoriteClick(item: NetworkItem) {
        viewModel.onFavoriteClicking(item)
    }


}