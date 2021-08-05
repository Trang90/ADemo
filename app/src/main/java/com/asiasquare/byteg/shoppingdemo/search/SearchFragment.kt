package com.asiasquare.byteg.shoppingdemo.search

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentSearchBinding
import com.asiasquare.byteg.shoppingdemo.util.onQueryTextChanged

class SearchFragment : Fragment(), SearchFragmentAdapter.OnClickListener {

    /** binding will only exist between onAttach and on Detach **/
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */
    private val viewModel: SearchFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, SearchFragmentViewModel.Factory(activity.application))
            .get(SearchFragmentViewModel::class.java)
    }


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
                if (viewModel.searchQuery.value.isNotEmpty()) {
                    binding.recyclerviewSearch.visibility = View.VISIBLE
                } else
                    binding.recyclerviewSearch.visibility = View.GONE
                adapter.submitList(it)
            }
        })

        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> Toast.makeText(context, "Đã thêm sản phẩm vào danh sách Yêu thích", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "Đã xóa sản phẩm khỏi danh sách Yêu thích", Toast.LENGTH_LONG).show()
            }
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
        inflater.inflate(R.menu.bottom_navigation_menu, menu)

        val searchItem = menu.findItem(R.id.searchFragment)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged { viewModel.searchQuery.value = it}
    }


    override fun onItemClick(search: LocalItem) {
        viewModel.onDetailClick(search)
    }

    override fun onAddFavoriteClick(item: LocalItem) {
        viewModel.onFavoriteClicking(item)
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}