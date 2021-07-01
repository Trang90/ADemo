package com.asiasquare.byteg.shoppingdemo.favorite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding?=null
    private val binding get()=_binding!!

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */

    private val viewModel: FavoriteFragmentViewModel by lazy {
        val activity =  requireNotNull(this.activity)
        ViewModelProvider(this, FavoriteFragmentViewModel.Factory(activity.application))
            .get(FavoriteFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container,false)

        /** Create recyclerView adapter and define OnClickListener **/
        val adapter = FavoriteFragmentAdapter(FavoriteFragmentAdapter.OnClickListener{
            Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
        })
        binding.recyclerViewYeuThich.adapter=adapter

        viewModel.favoriteList.observe(viewLifecycleOwner, Observer{
            it?.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
