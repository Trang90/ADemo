package com.asiasquare.byteg.shoppingdemo.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentCatalogBinding
import java.util.*

/**
First fragment of the app.
Display a list of catalog for user to choose.
 **/
class CatalogFragment : Fragment() {

    /** binding will only exist between onAttach and on Detach **/
    private var _binding : FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    var viewFlipper: ViewFlipper? = null

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */
    private val viewModel: CatalogFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,CatalogFragmentViewModel.Factory(activity.application))
            .get(CatalogFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCatalogBinding.inflate(inflater,container,false)


        /** Create recyclerView adapter and define OnClickListener **/
        val adapter = CatalogFragmentAdapter(CatalogFragmentAdapter.OnClickListener{
            viewModel.onCatalogClick(it)
        })

//        val args = CatalogFragmentArgs.fromBundle(requireArguments())
//        Toast.makeText(context, "Catalog ID: ${args.catalogId}", Toast.LENGTH_LONG).show()


        binding.rvMainCatalog.adapter = adapter

        /** Update data to adapter **/
        viewModel.catalogList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        /** Navigate to list of catalog by Id **/
        viewModel.navigateToCatalog.observe(viewLifecycleOwner, Observer { catalog ->
            catalog?.let {
                this.findNavController().navigate(
                    CatalogFragmentDirections.actionCatalogFragmentToItemListFragment(catalog.id, catalog.name)
                )
                viewModel.onNavigationComplete()
            }

        })

        viewFlipper = binding.viewflipper
        actionViewFlipper()
        return binding.root
    }


    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actionViewFlipper() {
        val mangquangcao = ArrayList<Int>()
        mangquangcao.add(R.drawable.banner05)
        for (i in mangquangcao.indices) {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageResource(mangquangcao[i])
            viewFlipper!!.addView(imageView)
        }
        viewFlipper!!.flipInterval = 10000
        viewFlipper!!.isAutoStart = true
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        val animationSlideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
        viewFlipper!!.inAnimation = animationSlideIn
        viewFlipper!!.outAnimation = animationSlideOut
    }
}