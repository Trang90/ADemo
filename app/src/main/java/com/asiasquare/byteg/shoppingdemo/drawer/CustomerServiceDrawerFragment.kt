package com.asiasquare.byteg.shoppingdemo.drawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentCustomerServiceDrawerBinding

class CustomerServiceDrawerFragment : Fragment() {
    private var _binding : FragmentCustomerServiceDrawerBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCustomerServiceDrawerBinding.inflate(inflater,container,false)

        binding.tvLienHeTitle.text = getText(R.string.thong_tin_lien_he)
        val tv : TextView = binding.tvLienHe
        tv.text = getText(R.string.thong_tin_lien_he_cam_on)
        tv.append("\n")
        tv.append("${getText(R.string.thong_tin_lien_he_bo_phan_cskh)}".trimIndent())
        tv.append("\n\n")
        tv.append(getText(R.string.thong_tin_lien_he_email))
        tv.append("\n"+ "${getText(R.string.thong_tin_lien_he_hotline)}".trimIndent())
        tv.append("\n")
        tv.append(getString(R.string.quad_tab))
        tv.append("${getText(R.string.thong_tin_lien_he_hotline_one)}".trimIndent())
        tv.append("\n")
        tv.append("${getText(R.string.thong_tin_lien_he_thoi_gian_title)}".trimIndent())
        tv.append("\n")
        tv.append(getString(R.string.quad_tab))
        tv.append("${getText(R.string.thong_tin_lien_he_thoi_gian_body_first_option)}".trimIndent())
        tv.append("\n\n")
        tv.append(getString(R.string.quad_tab))
        tv.append("${getText(R.string.thong_tin_lien_he_hotline_two)}".trimIndent())
        tv.append("\n")
        tv.append(getString(R.string.quad_tab))
        tv.append("${getText(R.string.thong_tin_lien_he_hotline_three)}".trimIndent())
        tv.append("\n")
        tv.append("${getText(R.string.thong_tin_lien_he_thoi_gian_title)}".trimIndent())
        tv.append("\n")
        tv.append(getString(R.string.quad_tab))
        tv.append("${getText(R.string.thong_tin_lien_he_thoi_gian_body_second_option)}".trimIndent())
        tv.append("\n\n")
        tv.append(getText(R.string.thong_tin_lien_he_end))
        return binding.root
    }


}