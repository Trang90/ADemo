package com.asiasquare.byteg.shoppingdemo.drawer

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentPaymentMethodDrawerBinding


class PaymentMethodDrawerFragment : Fragment() {
    private var _binding : FragmentPaymentMethodDrawerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentMethodDrawerBinding.inflate(inflater,container,false)
        binding.tvThanhToan.setText(R.string.thanh_toan_bang_the_tin_dung)
        binding.tvThanhToan.append("\n")
        binding.tvThanhToan.append(getString(R.string.visa_master))


        val tvCaC : TextView = binding.tvClickAndCollect

        tvCaC.text = getText(R.string.thong_tin_thanh_toan_body_cac)
        tvCaC.append("\n\n")
        val diaChi = getText(R.string.dia_chi_cua_hang).toString()
        tvCaC.append(setTextBold(diaChi, 0, diaChi.length))
        tvCaC.append("\n")
        tvCaC.append("\n"+ "${getText(R.string.thoi_gian_hoat_dong_t2_t6)}".trimIndent())
        tvCaC.append("\n"+ "${getText(R.string.thoi_gian_hoat_dong_t7)}".trimIndent())
        tvCaC.append("\n"+ "${getText(R.string.thoi_gian_hoat_dong_cn)}".trimIndent())


        return binding.root
    }

    //Make part of the text bold
    private fun setTextBold(string: String, begin: Int, end: Int): SpannableStringBuilder? {
        val sb = SpannableStringBuilder(string)
        val ssBold = StyleSpan(Typeface.BOLD) // Span to make text bold
        sb.setSpan(ssBold, begin, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return sb
    }
}