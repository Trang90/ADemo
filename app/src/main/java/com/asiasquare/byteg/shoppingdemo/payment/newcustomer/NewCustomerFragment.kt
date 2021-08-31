package com.asiasquare.byteg.shoppingdemo.payment.newcustomer

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentNewCustomerBinding
import com.asiasquare.byteg.shoppingdemo.util.isValidEmailAddress



class NewCustomerFragment : Fragment() {

    private var _binding : FragmentNewCustomerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewCustomerViewModel by lazy {
        val activity = requireNotNull(this.activity){

        }
        ViewModelProvider(this,NewCustomerViewModel.Factory(activity.application)).get(NewCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewCustomerBinding.inflate(inflater,container,false)

        /** Validate info before create customer **/
        binding.btCreate.setOnClickListener {
            var haveAllInfo : Boolean = true
            if(TextUtils.isEmpty(binding.etName.text)){
                binding.tfName.error = "*Xin vui lòng nhập tên"
                haveAllInfo = false
            }
            if(!binding.etEmailAddress.isValidEmailAddress()){
                binding.tfEmail.error = "*Địa chỉ mail không phù hợp"
                haveAllInfo = false
            }
            if(TextUtils.isEmpty(binding.etPhoneNumber.text)){
                binding.tfPhone.error = "*Xin vui lòng nhập số điện thoại liên lạc"
                haveAllInfo = false
            }
            if(TextUtils.isEmpty(binding.etAddressLine.text)){
                binding.tfAddressLine.error = "*Xin vui lòng nhập địa chỉ"
                haveAllInfo = false
            }
            if(TextUtils.isEmpty(binding.etAddressCity.text)){
                binding.tfAddressCity.error = "*Xin vui lòng nhập thành phố"
                haveAllInfo = false
            }
            if(TextUtils.isEmpty(binding.etAddressPostal.text)){
                binding.tfAddressPostal.error = "*Bắt buộc"
                haveAllInfo = false
            }
            viewModel.updateInfo(haveAllInfo)
        }

        /** Create customer when all info have been collected**/
        viewModel.haveAllInfo.observe(viewLifecycleOwner, Observer {
            if(it == true){
                createCustomer()
                viewModel.onFinishCreateCustomer()
            }
        })

        /** Hide error when user type on edittext**/
        binding.apply {
            etName.doOnTextChanged { _, _, _, _ ->
                binding.tfName.error = null
            }
            etPhoneNumber.doOnTextChanged { _, _, _, _ ->
                binding.tfPhone.error = null
            }
            etEmailAddress.doOnTextChanged { _, _, _, _ ->
                binding.tfEmail.error = null
            }
            etAddressCity.doOnTextChanged { _, _, _, _ ->
                binding.tfAddressCity.error =null
            }
            etAddressLine.doOnTextChanged { _, _, _, _ ->
                binding.tfAddressLine.error =null
            }
            etAddressPostal.doOnTextChanged { _, _, _, _ ->
                binding.tfAddressPostal.error =null
            }
        }

        /**Debug only*/
        viewModel.response.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvHeader.text = it.toString()
            }
        })

        /**Navigate to list of customer**/
        viewModel.navigateToCustomerList.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    NewCustomerFragmentDirections.actionNewCustomerFragmentToCustomerListFragment()
                )
                viewModel.onCompleteNavigateToCustomerList()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it == true){
                binding.processBar.visibility = View.VISIBLE
            }else{
                binding.processBar.visibility = View.INVISIBLE
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun createCustomer(){
        viewModel.createCustomer(
            name = binding.etName.text.toString(),
            email=binding.etEmailAddress.text.toString(),
            phone = binding.etPhoneNumber.text.toString() ,
            addressLine = binding.etAddressLine.text.toString() ,
            addressCity = binding.etAddressCity.text.toString(),
            addressPostal = binding.etAddressPostal.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}