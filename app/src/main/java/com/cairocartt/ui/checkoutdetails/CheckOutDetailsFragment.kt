package com.cairocartt.ui.checkoutdetails

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.cairocartt.R
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.RequestCreateOrder
import com.cairocartt.databinding.FragmentCheckOutDetailsBinding
import com.cairocartt.databinding.FragmentCreateOrderBinding
import com.cairocartt.ui.congratulition.CongratulitionCartActivity
import com.cairocartt.ui.createorder.CreateOrderViewModel
import com.cairocartt.ui.ordersuccess.OrderSuccessActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.row_hero_product.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckOutDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



@AndroidEntryPoint
class CheckOutDetailsFragment : BaseDialogFragment<FragmentCheckOutDetailsBinding>() {
    private var token: String? = String()
    override var idLayoutRes: Int=R.layout.fragment_check_out_details
    private lateinit var listCart: ListCartResponse.Data
    private  var paymentMethod:String="cashondelivery"
    private lateinit var Email:String
    private var data: SharedData? = null
    private var quta_id: String? = String()

    val mViewModel: CreateOrderViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        getData()
        checkRadioButton()
        onClickOrder()
        subscribeCheckOut()
    }
    private fun onClickOrder(){
        mViewDataBinding.BtnOrder.setOnClickListener(){
            if(isLogin()){
                mViewModel.createOrder(request = RequestCreateOrder(Email,RequestCreateOrder.PaymentMethod(paymentMethod)),
                    token = token,cart_id = null
                )
            }else {
                mViewModel.createOrder(request = RequestCreateOrder(Email,RequestCreateOrder.PaymentMethod(paymentMethod)),
                    token = null,cart_id = quta_id ,
                )
            }


        }
    }
    private fun isLogin(): Boolean {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }
    private fun subscribeCheckOut() {
        mViewModel.createOrderResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    data?.putValue("quta_id","")
                    var intent =Intent(requireContext(),OrderSuccessActivity::class.java)
                    intent.putExtra("id",it.data?.data)
                    startActivity(intent)

                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun init() {
        data = SharedData(requireContext())
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        quta_id=data?.getValue(SharedData.ReturnValue.STRING, "quta_id")

    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        var bundle: Bundle? = Bundle()
        bundle = this.arguments
        Email=bundle?.getString("Email")!!
        listCart = bundle?.getParcelable("listCart")!!
        checkDiscountVisablity(bundle.getString("Discount")!!)
        mViewDataBinding.model=listCart
        mViewDataBinding.TCountry.text=bundle.getString("country_Name")
        mViewDataBinding.TCity.text=bundle.getString("city_Name")
        mViewDataBinding.TAddress.text=bundle.getString("Address")
        mViewDataBinding.TEmail.text=bundle.getString("Email")
        mViewDataBinding.TPhone.text=bundle.getString("phone")
        mViewDataBinding.TDiscount.text=bundle.getString("Discount")+" "+resources.getString(R.string.currency)
        mViewDataBinding.TotalOrder.text=bundle.getString("TotalPrice")+" "+resources.getString(R.string.currency)
        mViewDataBinding.TTax.text=bundle.getString("tax")+" "+resources.getString(R.string.currency)
        mViewDataBinding.Shipping.text=bundle.getString("ShippingPrice")+" "+resources.getString(R.string.currency)
        mViewDataBinding.TDiscount.setPaintFlags(mViewDataBinding.TDiscount.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

    }
    private fun checkDiscountVisablity(discount:String){
        if(discount.equals("0.0")){
            mViewDataBinding.TDiscount.visibility=View.GONE
            mViewDataBinding.diescount.visibility=View.GONE
        }
    }
    private fun checkRadioButton(){
        var id: Int = mViewDataBinding.radios.getCheckedRadioButtonId()
        val radioButton: View = mViewDataBinding.radios.findViewById(id)
        val radioId = mViewDataBinding.radios.indexOfChild(radioButton)
        val btn = mViewDataBinding.radios.getChildAt(radioId) as RadioButton
        val selection = btn.text as String
        if (selection == "Cash On Delivery"  || selection == "نقدي") {
            paymentMethod="cashondelivery"
        }else {
            paymentMethod="payfort_fort_cc"
        }

    }

}