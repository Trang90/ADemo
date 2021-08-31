package com.asiasquare.byteg.shoppingdemo.payment.thanhtoantindung

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.GRAY
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asiasquare.byteg.shoppingdemo.MainActivity
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.Settings
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.databinding.ActivityThanhToanTinDungBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class ThanhToanTinDung : AppCompatActivity() {
    private lateinit var binding: ActivityThanhToanTinDungBinding
    private lateinit var thanhToanTinDungViewModel: ThanhToanTinDungViewModel

    private var backendUrl : String = ""
    private var httpClient = OkHttpClient()
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe
    private var mTongTien : Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThanhToanTinDungBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val application = requireNotNull(this).application
        val dataSource = AsiaDatabase.getInstance(this).basketItemDao
        val viewModelFactory = ThanhToanTinDungViewModelFactory(dataSource,application)

        thanhToanTinDungViewModel = ViewModelProvider(this,viewModelFactory).get(ThanhToanTinDungViewModel::class.java)

        thanhToanTinDungViewModel.tongTien.observe(this, Observer {
            mTongTien = it
        })

        val settings = Settings(this)
        backendUrl = settings.backendUrl
        stripe = Stripe(applicationContext, settings.publishableKey)
        Toast.makeText(this, "PB: ${settings.publishableKey}", Toast.LENGTH_SHORT).show()


        httpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        thanhToanTinDungViewModel.loaded.observe(this, Observer { loaded ->
            if(loaded){
                startCheckout()
            }
        })



    }

    private fun displayAlert(
            activity: Activity,
            title: String,
            message: String,
    ) {
        runOnUiThread {
            val builder = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)

            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    private fun displaySuccessDialog(
            activity: Activity,
            title: String,
            message: String,
    ) {
        runOnUiThread {
            val builder = MaterialAlertDialogBuilder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create().show()
        }
    }

    private fun startLoading(){
        binding.payButton.disable()
        binding.cardInputWidget.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.backButton.visibility = View.INVISIBLE

    }

    private fun stopLoading(){
        binding.cardInputWidget.isEnabled = false
        binding.progressBar.visibility = View.INVISIBLE
        binding.payButton.visibility = View.INVISIBLE
        binding.backButton.visibility = View.VISIBLE
        binding.backButton.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            // set the new task and clear flags
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

    }

    private fun startCheckout() {
        val weakActivity = WeakReference<Activity>(this)
        // Create a PaymentIntent by calling your server's endpoint.
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonString : String = thanhToanTinDungViewModel.getJsonString()

        val body = jsonString.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(backendUrl + "create-payment-intent")
                .post(body)
                .build()
        httpClient.newCall(request)
                .enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        weakActivity.get()?.let { activity ->
                            displayAlert(activity, "Failed to load page 1", "Error: $e")
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            weakActivity.get()?.let { activity ->
                                displayAlert(
                                        activity,
                                        "Failed to load page 2",
                                        "Error: $response"
                                )
                            }
                        } else {
                            val responseData = response.body?.string()
                            val responseJson =
                                    responseData?.let { JSONObject(it) } ?: JSONObject()

                            // For added security, our sample app gets the publishable key
                            // from the server.
                            paymentIntentClientSecret = responseJson.getString("clientSecret")
                        }
                    }
                })

        // Hook up the pay button to the card widget and stripe instance
        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            startLoading()
            val cardInputWidget =
                    findViewById<CardInputWidget>(R.id.cardInputWidget)
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    private fun getJsonString(): String {
        val payMap = mutableMapOf<String,Any>()
        val itemMap = mutableMapOf<String,Any>()
        val itemList = mutableListOf<MutableMap<String,Any>>()
        payMap["currency"] = "eur"
        itemMap["id"] = "M0001"
        itemMap["price"] = (mTongTien*100).roundToInt()
        itemList.add(itemMap)
        payMap["items"] = itemList
        val gson = Gson()
        return gson.toJson(payMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    weakActivity.get()?.let { activity ->
//                        displayAlert(
//                                activity,
//                                "Payment succeeded",
//                                gson.toJson(paymentIntent)
//                        )
                        stopLoading()
                        displaySuccessDialog(
                                activity,
                                "Payment succeeded",
                                "Total: $mTongTien $"
                        )
                    }
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    weakActivity.get()?.let { activity ->
                        displayAlert(
                                activity,
                                "Payment failed",
                                paymentIntent.lastPaymentError?.message.orEmpty()
                        )
                        stopLoading()
                    }
                }
            }
            override fun onError(e: Exception) {
                weakActivity.get()?.let { activity ->
                    displayAlert(
                            activity,
                            "Payment failed",
                            e.toString()
                    )
                    stopLoading()
                }
            }
        })
    }

    private fun View.disable() {
//        background.setColorFilter(GRAY, PorterDuff.Mode.MULTIPLY)
        setBackgroundColor(Color.GRAY)
        isClickable = false
    }

    private fun View.enable(){
        background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(context, R.color.colorAccent),BlendModeCompat.SRC_ATOP)
        isClickable = true
    }
}