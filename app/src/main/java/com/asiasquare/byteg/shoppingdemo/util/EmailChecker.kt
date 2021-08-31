package com.asiasquare.byteg.shoppingdemo.util

import android.util.Patterns
import android.widget.EditText

/** Check if email is valid **/
fun EditText.isValidEmailAddress() : Boolean{
    val email = this.text.toString()
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}