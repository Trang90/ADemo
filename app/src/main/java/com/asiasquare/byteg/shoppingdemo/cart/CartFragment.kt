package com.asiasquare.byteg.shoppingdemo.cart


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    /** binding will only exist between onAttach and on Detach **/
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */
    private val viewModel: CartFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,CartFragmentViewModel.Factory(activity.application))
            .get(CartFragmentViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater,container,false)

        /** Create recyclerView adapter and define OnClickListener **/
        val adapter = CartFragmentAdapter(CartFragmentAdapter.OnClickListener{
            Toast.makeText(context, it.textTenSanPham, Toast.LENGTH_SHORT).show()
        })

        binding.recyclerViewGioHang.adapter = adapter

        /** Update data to adapter **/
        viewModel.cartList.observe(viewLifecycleOwner, Observer {
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