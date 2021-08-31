package com.asiasquare.byteg.shoppingdemo.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentSearchBinding
import com.asiasquare.byteg.shoppingdemo.favorite.FavoriteFragmentAdapter
import com.asiasquare.byteg.shoppingdemo.favorite.FavoriteFragmentDirections
import com.asiasquare.byteg.shoppingdemo.util.onQueryTextChanged

class SearchFragment : Fragment(), SearchFragmentAdapter.OnClickListener {

    /** binding will only exist between onAttach and on Detach **/
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchView : SearchView
    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */
    private val viewModel: SearchFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, SearchFragmentViewModel.Factory(activity.application))
            .get(SearchFragmentViewModel::class.java)
    }

    private var toast : Toast? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater,container,false)

        /** Create recyclerView adapter **/

        val adapter = SearchFragmentAdapter(this)

        binding.recyclerviewSearch.adapter = adapter

        /** Update data to adapter **/
        viewModel.searchItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (viewModel.searchQuery.value.isNotEmpty()) {
                    binding.recyclerviewSearch.visibility = View.VISIBLE
                }
                else
                    binding.recyclerviewSearch.visibility = View.GONE

                if (it.isEmpty()) {
                    binding.findEmptyTv.visibility = View.VISIBLE
                }else
                    binding.findEmptyTv.visibility = View.GONE


            }
        })
        /** weird behavior **/
        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            showToast(it)

        })

        /** Navigate to detail by Id **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_action_search, menu)

        val sort = menu.findItem(R.id.sortItem)
        sort.isVisible = false

        val searchItem = menu.findItem(R.id.searchFragment)
        searchView = searchItem.actionView as SearchView
        searchItem.expandActionView() // automatically expand searchView
        searchView.maxWidth = Integer.MAX_VALUE // close button alignment
        //Restoring the SearchView when rotation
        val pendingQuery = viewModel.searchQuery.value

        if (pendingQuery != null && pendingQuery.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }


        searchView.onQueryTextChanged { viewModel.searchQuery.value = it}

    }


    override fun onItemClick(item: LocalItem) {
        viewModel.onDetailClick(item)
    }

    override fun onAddFavoriteClick(item: LocalItem) {
        viewModel.onFavoriteClicking(item)
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

    fun showToast(isFavorite: Boolean){
        toast?.cancel()

        toast = when(isFavorite){
            true -> Toast.makeText(context, "Đã thêm sản phẩm vào danh sách Yêu thích", Toast.LENGTH_SHORT)

            else -> Toast.makeText(context, "Đã xóa sản phẩm khỏi danh sách Yêu thích", Toast.LENGTH_SHORT)
        }
        toast?.show()
    }

}