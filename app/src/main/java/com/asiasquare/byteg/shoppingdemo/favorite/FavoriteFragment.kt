package com.asiasquare.byteg.shoppingdemo.favorite

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect


class FavoriteFragment : Fragment(), FavoriteFragmentAdapter.OnClickListener {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */

    private val viewModel: FavoriteFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, FavoriteFragmentViewModel.Factory(activity.application))
            .get(FavoriteFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        /** Create recyclerView adapter and define OnClickListener **/
        val adapter = FavoriteFragmentAdapter(this)
        binding.recyclerViewYeuThich.adapter = adapter

        viewModel.favoriteList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                } else
                    binding.emptyView.visibility = View.GONE
                adapter.submitList(it)
            }
        })

        /** Navigate to detail by Id **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
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
        }).attachToRecyclerView(binding.recyclerViewYeuThich)

        /** Undo delete functionality **/
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is FavoriteFragmentViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
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

    override fun onItemClick(favorite: FavoriteItem) {
        viewModel.onDetailClick(favorite)
    }



    override fun onDeleteClick(favorite: FavoriteItem) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())

        // set message of alert dialog
        dialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Xác nhận", DialogInterface.OnClickListener { dialog, id ->
                viewModel.onDeleteFavoriteClicking(favorite)
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

    override fun addToCard(favorite: FavoriteItem) {
        viewModel.onCartClicking(favorite)
        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

}
