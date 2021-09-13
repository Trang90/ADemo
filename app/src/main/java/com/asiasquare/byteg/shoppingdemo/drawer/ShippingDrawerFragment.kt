package com.asiasquare.byteg.shoppingdemo.drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentPaymentMethodDrawerBinding
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentShippingDrawerBinding


class ShippingDrawerFragment : Fragment() {
    private var _binding : FragmentShippingDrawerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShippingDrawerBinding.inflate(inflater,container,false)
        binding.tvGiaoHangFirst.text = getText(R.string.thong_tin_giao_hang_body_duoi_50)
        binding.tvGiaoHangSecond.text = getText(R.string.thong_tin_giao_hang_body_tren_50)
        binding.tvGiaoHangThird.text = getText(R.string.thong_tin_giao_hang_body_lien_he)

        return binding.root
    }
}