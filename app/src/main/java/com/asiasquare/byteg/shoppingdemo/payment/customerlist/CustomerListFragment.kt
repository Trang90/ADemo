package com.asiasquare.byteg.shoppingdemo.payment.customerlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.customers.Customer
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentCustomerListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomerListFragment : Fragment(),CustomerListAdapter.OnClickListener {

    private var _binding : FragmentCustomerListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CustomerListViewModel by lazy {
        val activity = requireNotNull(this.activity){
        }
        ViewModelProvider(this,CustomerListViewModel.Factory(activity.application)).get(CustomerListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerListBinding.inflate(inflater,container,false)

        val adapter = CustomerListAdapter(this)
        (activity as AppCompatActivity).supportActionBar?.title = "Profiles"
        binding.rvCustomerList.layoutManager
        binding.rvCustomerList.adapter = adapter

        viewModel.customerList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        viewModel.navigateToCustomer.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    CustomerListFragmentDirections.actionCustomerListFragmentToUIActivity(it.customerId)

                )
                viewModel.onCompleteNavigateToCustomer()
            }
        })

        binding.btAddNew.setOnClickListener {
            this.findNavController().navigate(
                CustomerListFragmentDirections.actionCustomerListFragmentToNewCustomerFragment()
            )
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(customer: Customer) {
        viewModel.chooseCustomer(customer)
    }

    override fun onDeleteClick(customer: Customer) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.xoa_thong_tin))
            .setMessage(resources.getString(R.string.xoa_thong_tin_detail))
            .setNegativeButton(resources.getString(R.string.quay_lai)){dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.xac_nhan)){dialog, which ->
                dialog.dismiss()
                viewModel.deleteCustomer(customer)
            }
            .show()
    }
}