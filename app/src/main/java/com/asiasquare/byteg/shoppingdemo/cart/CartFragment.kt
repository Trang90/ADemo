package com.asiasquare.byteg.shoppingdemo.cart


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class CartFragment : Fragment(), CartFragmentAdapter.OnClickListener {

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
        val adapter = CartFragmentAdapter(this)
        binding.recyclerViewGioHang.adapter = adapter

//        val adapter = CartFragmentAdapter(CartFragmentAdapter.OnClickListener{
//            Toast.makeText(context, it.textTenSanPham, Toast.LENGTH_SHORT).show()
//        })

        binding.recyclerViewGioHang.adapter = adapter

        /** Update data to adapter **/
        viewModel.cartList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.buttonDatHang.visibility = View.GONE
                } else
                    binding.emptyView.visibility = View.GONE
                adapter.submitList(it)
            }
        })

        /** Swipe delete functionality **/
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = adapter.currentList[viewHolder.absoluteAdapterPosition]
                viewModel.onTaskSwiped(task)
            }
        }).attachToRecyclerView(binding.recyclerViewGioHang)

        /** Undo delete functionality **/
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is CartFragmentViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Đã xóa sản phẩm", Snackbar.LENGTH_LONG)
                            .setAction("Hoàn tác") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                }
            }
        }
        return binding.root
    }

    /**Remove _binding when fragment is destroy**/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onDeleteClick(cart: ShoppingBasketItem) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())

        // set message of alert dialog
        dialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Xác nhận", DialogInterface.OnClickListener { dialog, id ->
                viewModel.onDeleteCartClicking(cart)
            })
            // negative button text and action
            .setNegativeButton("Quay lại", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Xác nhận xóa")
        // show alert dialog
        alert.show()

    }

    override fun onAddClick(cart: ShoppingBasketItem) {
        viewModel.onAddBtnClicking(cart)
    }

    override fun onMinusClick(cart: ShoppingBasketItem) {
        viewModel.onMinusBtnClicking(cart)
    }


}