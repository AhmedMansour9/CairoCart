package com.cairocartt.ui.orderdetails

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.adapter.CartOrderAdapter
import com.cairocartt.adapter.CartOrderDetailsAdapter
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.data.remote.model.RequestCreateOrder
import com.cairocartt.databinding.FragmentMyOrderDetailsBinding
import com.cairocartt.ui.createorder.CreateOrderViewModel
import com.cairocartt.ui.ordersuccess.OrderSuccessActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_myorder.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyOrderDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyOrderDetails : BaseDialogFragment<FragmentMyOrderDetailsBinding>() {
    private var token: String? = String()
    override var idLayoutRes: Int=R.layout.fragment_my_order_details
    private lateinit var listCart: MyOrdersResponse.Data
    private lateinit var Email:String

    private var data: SharedData? = null
    private var quta_id: String? = String()
    private lateinit var cartAdapter: CartOrderDetailsAdapter

    val mViewModel: CreateOrderViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initRecycle()
        getData()
        finish()

    }

    private fun finish() {
        mViewDataBinding.ImgClose.setOnClickListener(){
            dismiss()
        }
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
        listCart = bundle?.getParcelable("list")!!
        Email= listCart.customerEmail.toString()
        mViewDataBinding.model=listCart
        mViewDataBinding.TEmail.text=listCart.customerEmail
        mViewDataBinding.TPhone.text=listCart.billingAddress?.telephone
        cartAdapter.setDeveloperList(listCart.mobileItems)
        setImageStatus(listCart.status.toString())
        setStatus(listCart)
    }

    @SuppressLint("SetTextI18n")
    private fun setStatus(position: MyOrdersResponse.Data?) {
        if(position?.status.equals("processing")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.processing)
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.processing)
        }
        else if(position?.status.equals("pending_payment")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.pending_payment)
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.pending_payment)

        }
        else if(position?.status.equals("pending")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.pending)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.pending)

        }
        else if(position?.status.equals("holded")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.holded)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.holded)

        }
        else if(position?.status.equals("complete")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.completed)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.completed)
        }
        else if(position?.status.equals("canceled")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.canceled)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.canceled)
        }
        else if(position?.status.equals("closed")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.closed)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.closed)
        }
        else if(position?.status.equals("payfort_fort_new")){
            mViewDataBinding.TOrderStatus.text=": "+resources.getString(R.string.payfort_payment)
            mViewDataBinding.TStatus.text=": "+resources.getString(R.string.payfort_payment)
        }
    }

    private fun initRecycle() {
        cartAdapter = CartOrderDetailsAdapter(requireContext())
        mViewDataBinding.orderRecyclerView.setHasFixedSize(true)
        mViewDataBinding.orderRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.orderRecyclerView.adapter = cartAdapter
    }
    private fun setImageStatus(status:String){

        if(status.equals("pending")){
//            root.T_order_status.text=context?.resources?.getString(R.string.pending)
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_pendingstatus)
        }
        else if(status.equals("inShipment")){
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_shipping)
//            root.T_order_status.text=context?.resources?.getString(R.string.shipping)
        }
        else if(status.equals("On arrival")){
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_arrival)
//            root.T_order_status.text=context?.resources?.getString(R.string.onarrival)
        }
        else if(status.equals("onDelivery")){
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_deliverystatus)
//            root.T_order_status.text=context?.resources?.getString(R.string.ondelivry)

        }
        else if(status.equals("completed")){
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_completedstatus)
//            root.T_order_status.text=context?.resources?.getString(R.string.completed)

        }
        else if(status.equals("canceled")){
            mViewDataBinding.ImageStatus.setImageResource(R.drawable.ic_canceled)
//            root.T_order_status.text=context?.resources?.getString(R.string.canceled)

        }

    }
}