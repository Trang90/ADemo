package com.asiasquare.byteg.shoppingdemo.testpackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentBlankBinding
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentItemListBinding


class BlankFragment : Fragment() {
    private var _binding : FragmentBlankBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BlankFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity){

        }
        ViewModelProvider(this,BlankFragmentViewModel.Factory(activity.application)).get(BlankFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBlankBinding.inflate(inflater,container,false)

        viewModel.size.observe(viewLifecycleOwner, Observer {
            binding.tvResultSize.text = it.toString()
        })
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.tvResultFirst.text = it.itemDescription.toString()
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}