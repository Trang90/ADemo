package com.asiasquare.byteg.shoppingdemo.payment.stripepayment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.asiasquare.byteg.shoppingdemo.MainActivity
import com.asiasquare.byteg.shoppingdemo.databinding.ActivityCheckoutUiBinding
import com.stripe.android.paymentsheet.PaymentSheet

internal class UIActivity : BaseUIActivity() {

    private lateinit var binding: ActivityCheckoutUiBinding
    private val args : UIActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.title = " Check out!"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        temporaryCustomerId = args.customerId

        viewModel.stripeAmount.observe(this, Observer {
            amount = it
        })

        val paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        viewModel.inProgress.observe(this, Observer {
            binding.processBar.isInvisible = !it
            binding.btCheckout.isEnabled = !it
        })

        val adapter = ItemListPaymentAdapter(ItemListPaymentAdapter.OnClickListener{

        })

        binding.rvItemList.adapter = adapter
        binding.rvItemList.addItemDecoration(
            DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL)
        )

        viewModel.itemInCartList.observe(this, Observer {
            it?.let {
                viewModel.countItem(it)
                adapter.submitList(it)
                viewModel.calcutalePrice(it)
            }
        })

        viewModel.status.observe(this, Observer {
            binding.tvInfo.text = it
        })

        viewModel.subTotal.observe(this, Observer {
            binding.tvSubtotal.text = it.toString()
        })
        viewModel.total.observe(this, Observer {
            binding.tvTotal.text = it.toString()
        })
        viewModel.shippingPrice.observe(this, Observer {
            binding.tvShipping.text = it.toString()
        })
        viewModel.itemsInList.observe(this, Observer {
            binding.tvInfo.text = "Có $it sản phẩm trong giỏ hàng"
        })
        viewModel.continueShopping.observe(this, Observer {
            if(it == true){
                binding.btCheckout.visibility= View.GONE
                binding.btReturn.visibility = View.VISIBLE

            }else{
                binding.btCheckout.visibility= View.VISIBLE
                binding.btReturn.visibility = View.INVISIBLE
            }
        })

        binding.btReturn.setOnClickListener {
            viewModel.onClear()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btCheckout.setOnClickListener {
            prepareCheckout { customerConfiguration, clientSecret ->
                paymentSheet.presentWithPaymentIntent(
                    clientSecret,
                    PaymentSheet.Configuration(
                        merchantDisplayName = merchantName,
                        customer = customerConfiguration,
                        googlePay = googlePayConfig,
                    )
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}