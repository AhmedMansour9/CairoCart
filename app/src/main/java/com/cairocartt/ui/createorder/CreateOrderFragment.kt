package com.cairocartt.ui.createorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.adapter.CartAdapter
import com.cairocartt.adapter.CartOrderAdapter
import com.cairocartt.adapter.ShippingMethodAdapter
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.*
import com.cairocartt.databinding.FragmentCreateOrderBinding
import com.cairocartt.ui.checkoutdetails.CheckOutDetailsFragment
import com.cairocartt.ui.editprofile.EditProfileViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class CreateOrderFragment : BaseDialogFragment<FragmentCreateOrderBinding>(), CreateOrderNavigator {
    private var token: String? = String()
    private var data: SharedData? = null
    var country_Id: String? = String()
    var city_Id: String? = String()
    var city_Code: String? = String()
    var city_Name: String? = String()
    var f_Name: String? = String()
    var L_Name: String? = String()
    var phone: String? = String()
    var Email: String? = String()
    var Address: String? = String()
    var TotalPrice: String? = String()
    var Country: String? = String()
    var ShippingPrice: String? = String()
    var Tax: String? = String()
    var Discount: String? = String()
    private var quta_id: String? = String()

    var shipping_Methid: String? = String()
    var shipping_Carrir: String? = String()
    private val mViewModelProfile: EditProfileViewModel by viewModels()

    var request = RequestShippingAddressModel()

    override var idLayoutRes: Int = R.layout.fragment_create_order
    private lateinit var listCart: ListCartResponse.Data

    val mViewModel: CreateOrderViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    private lateinit var cartAdapter: CartOrderAdapter

    private var shipAdapter = ShippingMethodAdapter(object : ShippingMethodAdapter.ShippingMethodItemListener {
            override fun itemClicked(productData: ListShippingMethod.Data) {
                shipping_Methid = productData.methodCode
                shipping_Carrir = productData.carrierCode
                mViewDataBinding.ShippingPrice.isVisible = true
                mViewDataBinding.ShippingPrice.text = productData.carrierTitle
                mViewDataBinding.Shipping.isVisible = true
                mViewDataBinding.Shipping.text =
                    productData.amount.toString() + " " + resources.getString(R.string.currency)

            }
        })


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        getProfile()
        initRecycle()
        getData()
        setupObserverProfile()
        subscribeCounrties()
        initRecycleShipping()
        subscribeShipping()
        subscribeCheckOut()
    }

    private fun initRecycle() {
        cartAdapter = CartOrderAdapter(requireContext())
        mViewDataBinding.orderRecyclerView.setHasFixedSize(true)
        mViewDataBinding.orderRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.orderRecyclerView.adapter = cartAdapter
    }

    private fun getData() {
        var bundle: Bundle? = Bundle()
        bundle = this.arguments
        listCart = bundle?.getParcelable("data")!!
        cartAdapter.setDeveloperList(listCart?.items as MutableList<ListCartResponse.Data.Item>)
        mViewDataBinding.model = listCart
        quta_id=data?.getValue(SharedData.ReturnValue.STRING, "quta_id")
    }

    private fun init() {
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
        data = SharedData(requireContext())
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")

    }


    private fun initRecycleShipping() {
        mViewDataBinding.shpipingRecyclerView.setHasFixedSize(true)
        mViewDataBinding.shpipingRecyclerView.setLayoutManager(
            LinearLayoutManager(
                requireContext()
            )
        )
        mViewDataBinding.shpipingRecyclerView.adapter = shipAdapter
    }

    fun setData(profile: ProfileResponse) {
        mViewDataBinding.EFName.setText(profile.data?.customer?.firstname.toString())
        mViewDataBinding.ELastname.setText(profile.data?.customer?.lastname.toString())
        mViewDataBinding.EEmail.setText(profile.data?.customer?.email.toString())

    }

    private fun setupObserverProfile() {
        mViewModelProfile.getProfileResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> setData(it1) }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        })
    }

    private fun subscribeCounrties() {
        mViewModel.countriesResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    bindingAdapter(it.data?.data as List<CountriesResponse.Data>)
                }
                Status.LOADING -> {
                    showLoading()

                }

                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
    }

    private fun bindingAdapter(List: List<CountriesResponse.Data>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_selected, List)
        mViewDataBinding.spinnerAdapter = adapter

        mViewModel.itemPositionCountry.observe(this, Observer { postion ->
            adapter.getItem(postion)?.id
            country_Id = "EG"
            Country=adapter.getItem(postion)?.fullNameLocale
            bindingAdapterCities(adapter.getItem(postion)?.availableRegions)

        })
    }

    private fun bindingAdapterCities(List: List<CountriesResponse.Data.AvailableRegion>?) {
        val adapter = List?.let {
            ArrayAdapter(
                requireContext(), R.layout.spinner_item_selected,
                it
            )
        }
        mViewDataBinding.spinnerAdapterCities = adapter
        mViewModel.itemPositionCity.observe(this, Observer { postion ->
            adapter?.getItem(postion)?.id
            city_Code = adapter?.getItem(postion)?.code
            city_Id = adapter?.getItem(postion)?.id
            city_Name = adapter?.getItem(postion)?.name

            var request = RequestShippingMethodResponse(
                RequestShippingMethodResponse.Address(
                    country_Id,
                    0,
                    adapter?.getItem(postion)?.name,
                    adapter?.getItem(postion)?.code,
                    adapter?.getItem(postion)?.id?.toInt()
                )
            )
           if(isLogin()){
               mViewModel.getShippingMethod(request, token,null)
           }else {
               mViewModel.getShippingMethod(request, null,quta_id)

           }
        })
    }

    private fun subscribeShipping() {
        mViewModel.shippingResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    setDataShipping(it.data?.data as MutableList<ListShippingMethod.Data>)
                }
                Status.LOADING -> {
                    showLoading()

                }

                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
    }

    private fun setDataShipping(list: MutableList<ListShippingMethod.Data>) {
        mViewDataBinding.shpipingRecyclerView.isVisible = true
        mViewDataBinding.shippingMethod.isVisible = true
        shipAdapter.setList(list)
    }

    override fun onCLickFinish() {
        dismiss()
    }

    override fun onClickNext() {
        if (checkOutValidation()) {
            val listAddress: MutableList<String>? = mutableListOf()
            listAddress?.add(Address!!)
            val requestaddress = RequestShippingAddressModel.AddressInformation.ShippingAddress(
                city_Name,
                country_Id,
                Email,
                f_Name,
                L_Name,
                city_Name,
                city_Code,
                city_Id?.toInt(),
                phone,
                listAddress
            )
            val requestaddressshipping = RequestShippingAddressModel.AddressInformation.BillingAddress(
                city_Name,
                country_Id,
                Email,
                f_Name,
                L_Name,
                city_Name,
                city_Code,
                city_Id?.toInt(),
                phone,
                listAddress
            )
            val requestshipping = RequestShippingAddressModel(
                RequestShippingAddressModel.AddressInformation(
                    requestaddress,
                    requestaddressshipping,
                    shipping_Carrir,
                    shipping_Methid
                )
            )
            if (isLogin()) {
                mViewModel.setShippingAddress(requestshipping, token,null)
            }else {
                mViewModel.setShippingAddress(requestshipping, null,quta_id)
            }
        }
    }
    private fun subscribeCheckOut() {
        mViewModel.shippingAddressResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    TotalPrice=it.data?.data?.totals?.grandTotal.toString()
                    ShippingPrice=it.data?.data?.totals?.shippingAmount.toString()
                    Tax=it.data?.data?.totals?.taxAmount.toString()
                    Discount=it.data?.data?.totals?.discountAmount.toString()
                    checkOut()
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
    private fun checkOut(){
        val newDialogFragment = CheckOutDetailsFragment()
        val bundle = Bundle()
        bundle.putString("country_Name",Country)
        bundle.putString("city_Name",city_Name)
        bundle.putString("phone",phone)
        bundle.putString("Email",Email)
        bundle.putString("Address",Address)
        bundle.putParcelable("listCart",listCart)
        bundle.putString("TotalPrice",TotalPrice)
        bundle.putString("ShippingPrice",ShippingPrice)
        bundle.putString("Discount",Discount)

        bundle.putString("tax",Tax)

        newDialogFragment.setArguments(bundle)
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        newDialogFragment.show(transaction, "New_Dialog_Fragment")
    }

    fun checkOutValidation(): Boolean {
        if (validateCity() && validateFName() && validateLName() && validateEmail() && validatePhone() && validateAddress() && validateShipping()) {
            return true
        }
        return false
    }

    fun validateCity(): Boolean {
        if (city_Id.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.please_enterstate),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    fun validateFName(): Boolean {
        f_Name = mViewDataBinding.EFName.text.toString()
        if (f_Name.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validatefname),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    fun validateLName(): Boolean {
        L_Name = mViewDataBinding.ELastname.text.toString()
        if (L_Name.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validateLname),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    fun validateEmail(): Boolean {
        Email = mViewDataBinding.EEmail.text.toString()
        if (Email.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validateEmail),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true

    }

    fun validatePhone(): Boolean {
        phone = mViewDataBinding.EPhone.text.toString()
        if (phone.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validatePhone),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }

    fun validateAddress(): Boolean {
        Address = mViewDataBinding.EAddress.text.toString()
        if (Address.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validateAddress),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true

    }

    fun validateShipping(): Boolean {
        if (shipping_Methid.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.validatemethod),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true

    }

    private fun isLogin(): Boolean {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun getProfile() {
        lifecycleScope.launch {
            if (isLogin())
                mViewModelProfile.getProfile(token!!)
        }
    }

}