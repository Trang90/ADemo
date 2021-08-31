package com.asiasquare.byteg.shoppingdemo.detail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentDetailBinding


class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailFragmentViewModel

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var item: LocalItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentDetailBinding.inflate(inflater,container,false)

        val application = requireNotNull(activity).application
        item = args.selectedItem
        val viewModelFactory = DetailFragmentViewModel.Factory(item, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailFragmentViewModel::class.java)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivCatalogGrid.load(item.itemImageSource)
            tenSanPham.text = item.itemName
            giaSanPham.text = "€"+item.itemPrice.toString()
            moTaSanPham.text = item.itemDescription
            khoiLuongSanPham.text = "Khối lượng: "+item.itemWeight
            sanPhamThuongHieu.text ="Thương hiệu: "+item.itemBrand
            sanPhamXuatXu.text= "Xuấtxứ: "+item.itemOrigin
        }

        /** Create spinner button **/
        val amount = arrayListOf<Int>()
        for (i in 1..10) {amount.add(i)}

        val arrayAdapter =
            context?.let { ArrayAdapter(it, R.layout.spinner_item_custom,amount) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter= arrayAdapter
        binding.spinner.onItemSelectedListener = this

        /** Change heart color: red if it's a favorite, black if it is not **/
        viewModel.isFavorite.observe(viewLifecycleOwner, {
            val checkFavorite = when(it){
                true -> R.drawable.timdo24
                else -> R.drawable.timden24
            }
            binding.ivFavorite.setImageResource(checkFavorite)
        })

        binding.ivFavorite.setOnClickListener {
            viewModel.onFavoriteClicking()
        }

        binding.buttonMua.setOnClickListener {
            viewModel.onCartClicking()
            Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position)
        try {
            viewModel.setAmount(item as Int)
        } catch (e: Exception){
            viewModel.setAmount(1) // Set amount to 1 (default) if there is error when set this value. Should not happen
        }
        Log.d("DetailFragment", "amount set to ${viewModel.getAmount()} ")
    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

