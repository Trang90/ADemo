package com.asiasquare.byteg.shoppingdemo.payment.method

import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentPaymentMethodBinding

class PaymentMethodFragment : Fragment() {

    private var _binding : FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PaymentMethodFragment()
    }

    private lateinit var viewModel: PaymentMethodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentMethodBinding.inflate(inflater,container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = AsiaDatabase.getInstance(application).localCustomerDatabaseDao
        val viewModelFactory = PaymentMethodViewModel.Factory(dataSource)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PaymentMethodViewModel::class.java)

        /**Check current payment method **/
        viewModel.paymentMethod.value = when(binding.radioGroup.checkedRadioButtonId){
            binding.buttonCard.id -> PaymentMethodViewModel.PAYMENT_CARD
            binding.buttonCnc.id -> PaymentMethodViewModel.PAYMENT_CASH
            else -> -1
        }

        /** Card payment selected **/
        binding.buttonCard.setOnClickListener {
            binding.tvThongTin.visibility = View.INVISIBLE
            viewModel.paymentMethod.value = PaymentMethodViewModel.PAYMENT_CARD
        }

        /** Cash payment selected **/
        binding.buttonCnc.setOnClickListener {
            binding.tvThongTin.text = getText(R.string.thong_tin_thanh_toan_tien_mat)
            binding.tvThongTin.append("\n\n")
            binding.tvThongTin.append(getText(R.string.dia_chi_cua_hang))
            binding.tvThongTin.append("\n")
            binding.tvThongTin.append("\n" + getText(R.string.thoi_gian_hoat_dong_t2_t6))
            binding.tvThongTin.append("\n" + getText(R.string.thoi_gian_hoat_dong_t7))
            binding.tvThongTin.append("\n" + getText(R.string.thoi_gian_hoat_dong_cn))
            binding.tvThongTin.setTypeface(null, Typeface.BOLD)
            binding.tvThongTin.visibility = View.VISIBLE;
            viewModel.paymentMethod.value = PaymentMethodViewModel.PAYMENT_CASH
        }

        /** Show checkout button depends on payment method **/
        viewModel.paymentMethod.observe(viewLifecycleOwner, Observer {
            when(it){
                PaymentMethodViewModel.PAYMENT_CARD -> {
                    binding.buttonTiepTucDatHangCard.isClickable = true
                    binding.buttonTiepTucDatHangCash.isClickable = true
                    binding.buttonTiepTucDatHangCard.visibility = View.VISIBLE
                    binding.buttonTiepTucDatHangCash.visibility = View.INVISIBLE
                }
                PaymentMethodViewModel.PAYMENT_CASH ->{
                    binding.buttonTiepTucDatHangCard.isClickable = true
                    binding.buttonTiepTucDatHangCash.isClickable = true
                    binding.buttonTiepTucDatHangCard.visibility = View.INVISIBLE
                    binding.buttonTiepTucDatHangCash.visibility =View.VISIBLE
                }
                else -> {
                    binding.buttonTiepTucDatHangCard.isClickable = false
                    binding.buttonTiepTucDatHangCash.isClickable = false
                }
            }
        })

        /**
         * Check if there's customer info on local database
         * yes -> to customer list fragment
         * no -> to new customer fragment
         */
        viewModel.countCustomer.observe(viewLifecycleOwner, Observer { rows ->
            if(rows>0){
                binding.buttonTiepTucDatHangCard.setOnClickListener {
                    viewModel.navigateToList()
                }
            }else{
                binding.buttonTiepTucDatHangCard.setOnClickListener {
                    viewModel.navigateToInfo()
                }
            }
        })

        binding.buttonTiepTucDatHangCash.setOnClickListener {
            viewModel.navigateToCashPayment()
        }

        /** Navigation **/
        // To new customer fragment
        viewModel.navigateToInfo.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    PaymentMethodFragmentDirections.actionPaymentMethodFragmentToNewCustomerFragment()
                )
                viewModel.onNavigateComplete()
            }
        })
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    PaymentMethodFragmentDirections.actionPaymentMethodFragmentToCustomerListFragment()
                )
                viewModel.onNavigateComplete()
            }
        })
        viewModel.navigateToCashPayment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    //PaymentMethodFragmentDirections.actionPaymentMethodFragmentToDatHangActivity()
                PaymentMethodFragmentDirections.actionPaymentMethodFragmentToOrderFragment()
                )
                viewModel.onNavigateComplete()
            }
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}