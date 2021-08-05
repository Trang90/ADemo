package com.asiasquare.byteg.shoppingdemo.util

import androidx.appcompat.widget.SearchView

//search function
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
           clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                listener(newText.trim())
            }
            return true
        }
    })
}