package com.asiasquare.byteg.shoppingdemo.payment.order

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asiasquare.byteg.shoppingdemo.MainActivity
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.databinding.FragmentOrderBinding
import com.asiasquare.byteg.shoppingdemo.payment.thanhtoantindung.ThanhToanTinDung
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class OrderFragment : Fragment(), OrderAdapter.OnClickListener {

    var picker: DatePickerDialog? = null
    private var timePicker: TimePickerDialog? = null

    var listOrder: MutableList<ShoppingBasketItem>? = null

    var idThanhToan = 0
    private val nf: NumberFormat = DecimalFormat("##.###")
    private var dayPicker = 0
    private var hourPicker = 0
    private var minutePicker = 0
    private var totalPriceOrder = 0.0
    var totalPrice = 0.0

    private lateinit var binding: FragmentOrderBinding


    private val viewModel: OrderViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, OrderViewModel.Factory(activity.application))
            .get(OrderViewModel::class.java)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater,container,false)

        idThanhToan = 2

        val config = resources.configuration
        if (config.smallestScreenWidthDp >= 720) binding.recyclerViewOrder.layoutManager =
            GridLayoutManager(this.activity, 2)
        else binding.recyclerViewOrder.layoutManager = LinearLayoutManager(this.activity)

        val adapter = OrderAdapter(this)

        binding.recyclerViewOrder.adapter = adapter


        if (idThanhToan != 2) {
            binding.llDatePicker.visibility = View.GONE
        }
        if (idThanhToan == 3) {
            binding.btnOrder.text = "Tiến hành đặt hàng"
            binding.editTextName.setText("Tran Quang Trung")
            binding.editTextAddress.setText("St. Martin Strasse 30")
            binding.editTextTextEmail.setText("fakeemail@fakemail.com")
            binding.editTextPhone.setText("0906885816")
        } else {
            binding.btnOrder.text = "Gửi Email đặt hàng"
        }

        //Hien edittext chon ngay khi hinh thuc thanh toan = CaC
        if (idThanhToan == 2) {
            binding.llDatePicker.visibility = View.VISIBLE

            //Lay time tu he thong
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minute = calendar[Calendar.MINUTE]

            //Chon ngay - DatePicker
            binding.editTextDatePicker.setOnClickListener { //date picker dialog
                picker = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                    binding.editTextDatePicker.setText(dayOfMonth.toString() + "." + (month + 1) + "." + year)
                    val pickedDay = Calendar.getInstance()
                    pickedDay[year, month] = dayOfMonth
                    dayPicker = pickedDay[Calendar.DAY_OF_WEEK]
                }, day, month, year)
                val currentDate = Calendar.getInstance()
                val datePicker = picker!!.datePicker
                currentDate.add(Calendar.DAY_OF_MONTH, 1)
                datePicker.minDate = currentDate.timeInMillis
                currentDate.add(Calendar.MONTH, 1)
                datePicker.maxDate = currentDate.timeInMillis
                picker!!.show()
            }
            binding.editTextHourPicker.setOnClickListener {
                timePicker = TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                    val minuteString = String.format("%02d", minute)
                    binding.editTextHourPicker.setText ("$hourOfDay:$minuteString")
                    hourPicker = hourOfDay
                    minutePicker = minute
                }, hour, minute, true)
                timePicker!!.setTitle("Chọn giờ lấy hàng:")
                timePicker!!.show()
            }
        }



        viewModel.itemInCartList.observe(viewLifecycleOwner, Observer {order ->
            order?.let{
                listOrder= order
                adapter.submitList(it)
                for (i in it.indices) {
                    totalPrice = it[i].itemPrice * it[i].itemAmount
                    totalPriceOrder += totalPrice
                }
                if (idThanhToan != 2 && totalPriceOrder < 50) {
                    totalPriceOrder += 5.99
                    binding.shippingFee.text = "Chi phí vận chuyển: 5,99€"
                    binding.shippingFee.visibility = View.VISIBLE
                } else if (idThanhToan != 2 && totalPriceOrder >= 50) {
                    binding.shippingFee.text = "Chi phí vận chuyển: 0,00€"
                    binding.shippingFee.visibility = View.VISIBLE
                } else {
                    binding.shippingFee.visibility = View.INVISIBLE
                }
                val totalPriceOrderString = "Tổng số tiền: €" + nf.format(totalPriceOrder)
                binding.totalPriceOfOrder.text = totalPriceOrderString

            }
        })


        binding.btnBuyMore.setOnClickListener {
            val intentMuaTiep = Intent(this.activity, MainActivity::class.java)
            startActivity(intentMuaTiep)
        }

        binding.btnOrder.setOnClickListener(View.OnClickListener {
            val name = binding.editTextName.text.toString().trim { it <= ' ' }
            val phoneNumber = binding.editTextPhone.text.toString().trim { it <= ' ' }
            val email = binding.editTextTextEmail.text.toString().trim { it <= ' ' }
            val address = binding.editTextAddress.text.toString().trim { it <= ' ' }
            val pickUpDate = binding.editTextDatePicker.text.toString().trim { it <= ' ' }
            val pickUpTime = binding.editTextHourPicker.text.toString().trim { it <= ' ' }


            //Kiem tra khach hang da nhap ngay gio lay hang chua
            val suitablePickedUpDate : Boolean
            val suitablePickedUpTime : Boolean
            if (idThanhToan == 2) {
                if (pickUpDate.isEmpty() || pickUpTime.isEmpty()) {
                    suitablePickedUpDate = false
                    Toast.makeText(context,"Xin chọn ngày giờ lấy hàng!",Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    suitablePickedUpTime = checkPickUpTime()
                    Log.d("Dat hang", "kiemTraGioLayHang$suitablePickedUpTime")
                    if (suitablePickedUpTime) {
                        suitablePickedUpDate = true
                    } else {
                        suitablePickedUpDate = false
                        return@OnClickListener
                    }
                }
            } else {
                suitablePickedUpDate = true
            }

            if (name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() && address.isNotEmpty()
                && OrderFragment.isValidEmail(email) && suitablePickedUpDate ) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.data = Uri.parse("mailto:")

                if (idThanhToan == 1) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Paypal-Order from $name")
                } else if (idThanhToan == 2) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, "CnC-Order from $name")
                }

                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("asiasquare.com@gmail.com"))
                var emailcontent =
                    "Họ và tên: $name\nEmail: $email\nSố điện thoại: $phoneNumber\nĐịa chỉ: $address\n"
                if (idThanhToan == 2) {
                    emailcontent += "Ngày lấy hàng: $pickUpDate - $pickUpTime\nDanh sách sản phẩm mua: \n"
                }

                var total = 0.0

                for (i in listOrder!!.indices) {
                    emailcontent += listOrder!![i].itemAmount.toString() + "x"
                    emailcontent += " " + listOrder!![i].itemName
                    val itemPrice: Double = listOrder!![i].itemPrice
                    emailcontent += "\n(Đơn giá sản phẩm: ${nf.format(itemPrice)}€)"
                    emailcontent += "\nThành tiền:  ${listOrder!![i].itemPrice * listOrder!![i].itemAmount}€\n"
                    total += listOrder!![i].itemPrice
                }


                    if (idThanhToan == 1 && total < 50) {
                        total += 5.99
                        emailcontent += "\nChi phí vận chuyển: 5,99€"
                    } else {
                        emailcontent += "\nChi phí vận chuyển: 0,00€"
                    }
                emailcontent += "\nTổng số tiền: ${nf.format(total)}"

                    if (idThanhToan == 1) {
                        emailcontent += "\nThanh toán qua Paypal đến asiasquare.com@gmail.com"
                    } else if (idThanhToan == 2) {
                        emailcontent += "\nThanh toán bằng tiền mặt khi nhận hàng tại cửa hàng: Feldmochinger Str. 36, 80992 München "
                    }


                intent.putExtra(Intent.EXTRA_TEXT, emailcontent)
                Log.d("Dat hang", "Truoc khi gui intent")
                if (idThanhToan == 3) {
                    val intentTinDung = Intent(this.activity, ThanhToanTinDung::class.java)
                    startActivity(intentTinDung)
                } else {
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        for (i in listOrder!!.indices) {
                            viewModel.onDelete(listOrder!![i])
                        }
                        val intentMuaTiep = Intent(context, MainActivity::class.java)
                        Log.d("Order Fragment", "Truoc khi gui intent mua tiep")
                        startActivity(intentMuaTiep)
                        Toast.makeText(context,"Tiến hành gửi Email đặt hàng", Toast.LENGTH_SHORT).show()
                        Log.d("Dat hang", "Sau khi gui intent mua tiep")
                        startActivity(intent)
                        Log.d("Dat hang", "Sau khi gui intent")
                    }
                }

            } else {
                if (suitablePickedUpDate) {
                    Toast.makeText(context,"Thông tin cá nhân còn thiếu hoặc địa chỉ email chưa đúng",
                        Toast.LENGTH_SHORT).show()
                } else {
                    checkPickUpTime()
                }
            }
        })

        /** Navigate to detail **/
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    OrderFragmentDirections.actionOrderFragmentToDetailFragment(it)
                )
                viewModel.onNavigationComplete()
            }
        })


        return binding.root

    }

    private fun checkPickUpTime(): Boolean {
        Log.d("Dat hang", "Kiem tra")
        return if (dayPicker < 1 || dayPicker > 7) {
            Toast.makeText(context, "Ngày lấy hàng không phù hợp!", Toast.LENGTH_SHORT).show()
            false
        } else if (dayPicker == 1) {
            Toast.makeText(context,"Cửa hàng đóng cửa vào Chủ Nhật, xin chọn một ngày khác!",Toast.LENGTH_SHORT).show()
            false
        } else if (dayPicker == 7) {
            if (hourPicker < 9 || hourPicker >= 18 && minutePicker > 0 || hourPicker > 19) {
                Toast.makeText(context,"Giờ lấy hàng Thứ Bảy là 9 giờ đến 18 giờ, xin chọn giờ trong khoảng này!",Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        } else {
            if (hourPicker < 9 || hourPicker >= 19 && minutePicker > 0 || hourPicker > 19) {
                Toast.makeText(context,"Giờ lấy hàng trong tuần từ thứ 2 đến thứ 6 là 9 giờ đến 19 giờ, xin chọn giờ trong khoảng này!",
                    Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }



    companion object {
        fun isValidEmail(email: String?): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    }
                }

    override fun onItemClick(cart: ShoppingBasketItem) {
        viewModel.onDetailClick(cart)
    }
}
