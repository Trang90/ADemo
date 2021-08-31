package com.asiasquare.byteg.shoppingdemo.itemlist

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentItemListBinding
import com.asiasquare.byteg.shoppingdemo.databinding.GridViewItemListBinding
import com.asiasquare.byteg.shoppingdemo.util.onQueryTextChanged
import okhttp3.internal.notifyAll
import kotlin.properties.Delegates


class ItemListFragment : Fragment(), ItemListFragmentAdapter.OnClickListener {

    private val args: ItemListFragmentArgs by navArgs()
    private var itemListCatalogId by Delegates.notNull<Int>()

    private var _binding : FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchView : SearchView
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
        viewModel.localItemList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


//        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
//            when(it){
//                true -> Toast.makeText(context, "Đã thêm sản phẩm vào danh sách Yêu thích", Toast.LENGTH_LONG).show()
//                else -> Toast.makeText(context, "Đã xóa sản phẩm khỏi danh sách Yêu thích", Toast.LENGTH_LONG).show()
//            }
//        })

        /** Navigate to detail by Id **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    ItemListFragmentDirections.actionItemListFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_action_search, menu)

        val searchItem = menu.findItem(R.id.searchFragment)
        searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE // close button alignment
        //Restoring the SearchView when rotation
        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onQueryTextChanged { viewModel.searchQuery.value = it}

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME

                Handler().postDelayed({
                    binding.recyclerViewCatalog.smoothScrollToPosition (0) // mention the position in place of 0
                }, 0)
                true
            }
            R.id.action_sort_by_price -> {
                viewModel.sortOrder.value = SortOrder.BY_PRICE

                Handler().postDelayed({
                    binding.recyclerViewCatalog.smoothScrollToPosition (0) // mention the position in place of 0
                }, 0)
                true
            }


            else -> super.onOptionsItemSelected(item)

        }
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

    override fun onItemClick(item: LocalItem) {
        viewModel.onDetailClick(item)
    }

    override fun onAddFavoriteClick(item: LocalItem) {
        viewModel.onFavoriteClicking(item)
    }


}