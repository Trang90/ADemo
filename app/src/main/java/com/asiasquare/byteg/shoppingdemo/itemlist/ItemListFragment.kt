package com.asiasquare.byteg.shoppingdemo.itemlist


import android.os.Bundle
import android.view.*
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


    //search function
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemListBinding.inflate(inflater,container,false)


        itemListCatalogId = args.catalogId

//        val activity = requireNotNull(this.activity)
//        viewModel = ViewModelProvider(this, ItemListFragmentViewModel.Factory(activity.application,itemList))
//            .get(ItemListFragmentViewModel::class.java)

        val activity = requireNotNull(this.activity)
        viewModel = ViewModelProvider(this, ItemListFragmentViewModel.Factory(activity.application,itemListCatalogId))
            .get(ItemListFragmentViewModel::class.java)

        /** Create recyclerView adapter and define OnClickListener **/
//        val adapter = ItemListFragmentAdapter(ItemListFragmentAdapter.OnClickListener{
//            viewModel.onDetailClick(it)
//        })

        val adapter = ItemListFragmentAdapter(this)


        binding.recyclerViewCatalog.adapter = adapter

        /** ERROR, LOADING Event **/
        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (viewModel.status.value) {
                    LOADING -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.loading_animation)
                    }
                    ERROR -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    }
                    DONE -> {
                        binding.statusImage.visibility = View.GONE
                        binding.progressBar.visibility= View.GONE
                    }
                }
            }
        })

        /** Update data to adapter **/
        viewModel.text.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        //Toast.makeText(context, "Catalog ID: ${args.catalogId}", Toast.LENGTH_LONG).show()


        /** Navigate to detail by Id **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    ItemListFragmentDirections.actionItemListFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
            }
        })




        //search function
        setHasOptionsMenu(true)

        return binding.root
    }

    //search function
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_fragment, menu)
//
//        val searchItem = menu.findItem(R.id.action_search)
//        searchView = searchItem.actionView as SearchView
//
//        val pendingQuery = viewModel.searchQuery.value
//        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
//            searchItem.expandActionView()
//            searchView.setQuery(pendingQuery, false)
//        }
//
//        searchView.onQueryTextChanged {
//            viewModel.searchQuery.value = it
//        }
//
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_sort_by_name -> {
//                //viewModel.onSortOrderSelected(SortOrder.BY_NAME)
//                true
//            }
//            R.id.action_sort_by_date_created -> {
//                //viewModel.onSortOrderSelected(SortOrder.BY_DATE)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: NetworkItem) {
        //TODO("Not yet implemented")
        viewModel.onDetailClick(item)
    }

    override fun onAddFavoriteClick(favorite: NetworkItem) {
        //TODO("Not yet implemented")

    }


}