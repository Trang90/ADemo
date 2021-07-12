package com.asiasquare.byteg.shoppingdemo.favorite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect


//class FavoriteFragment : Fragment() {
class FavoriteFragment : Fragment(), FavoriteFragmentAdapter.OnClickListener {
    private var _binding: FragmentFavoriteBinding?=null
    private val binding get()=_binding!!

    /**
     * Create viewModel, provide application to Factory to create an AndroidViewModel class
     */

    private val viewModel: FavoriteFragmentViewModel by lazy {
        val activity =  requireNotNull(this.activity)
        ViewModelProvider(this, FavoriteFragmentViewModel.Factory(activity.application))
            .get(FavoriteFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container,false)

//        /** Create recyclerView adapter and define OnClickListener **/
//        val adapter = FavoriteFragmentAdapter(FavoriteFragmentAdapter.OnClickListener{
//            Toast.makeText(context, "${it.itemId} - ${it.itemName}", Toast.LENGTH_SHORT).show()
//        })

        val adapter = FavoriteFragmentAdapter(this)

        binding.recyclerViewYeuThich.adapter=adapter



        viewModel.favoriteList.observe(viewLifecycleOwner, Observer{
            it?.let {
                adapter.submitList(it)
            }
        })

        /** Swipe delete functionality **/
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = adapter.currentList[viewHolder.adapterPosition]
                viewModel.onTaskSwiped(task)
            }
        }).attachToRecyclerView(binding.recyclerViewYeuThich)

        /** Undo delete functionality **/
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event->
                when (event){
                    is FavoriteFragmentViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Favorite Item is deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
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
        TODO("Toan bo item")
    }

    override fun onDeleteClick(favorite: FavoriteItem) {
        //TODO("Nut xoa o day")
        viewModel.onDeleteFavoriteClicking(favorite)
    }

}

