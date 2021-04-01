package com.cairocartt.ui.detailsproduct

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.adapter.*
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.*
import com.cairocartt.databinding.FragmentDetailsProductBinding
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.addreview.AddReviewFragment
import com.cairocartt.ui.congratulition.CongratulitionCartActivity
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.policydetails.PolicyActivitiy
import com.cairocartt.ui.productsbyId.ProductsByIdViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


@AndroidEntryPoint
class DetailsProductFragment : BaseDialogFragment<FragmentDetailsProductBinding>(),
    DetailsProductNavigtor {
    override var idLayoutRes: Int = R.layout.fragment_details_product
    private lateinit var details: ProductsResponse.Data
    private var bundle = Bundle()
    private val productViewModel: DetailsProductViewModel by viewModels()
    private val revwiesAdapter = ReviwesAdapter()
    private var data: SharedData? = null
    private var token: String? = String()
    private var counter: Int = 1
    private var totalPrice: Int = 1
    private var quataId: String? = String()
    private lateinit var productsGridAdapter : RelatedAdapter
    private var searchJob: Job? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        initAdapter()
        getData()
        setData()
        initUI()
        initGridUI()
        initSlider()
        getReviwes()
        initInformation()
        checkStock()
        subscribeReviwes()
        subscribeAddFavourit()
        subscriberemoveFavourit()
        subscribeAddToCart()
        subscribeCreateCart()
        subscribeaddGustCart()
        onClickAddCart()
        setupView()
        checkRelatedProduct()



    }

    private fun setupView() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            productViewModel.listData.collect {
                productsGridAdapter.submitData(it)
            }
        }

    }
    private fun initGridUI() {
        mViewDataBinding.recyclerProductsRelated.isMotionEventSplittingEnabled=false
        mViewDataBinding.recyclerProductsRelated.setLayoutManager(LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false))
        mViewDataBinding.recyclerProductsRelated.adapter = productsGridAdapter
        mViewDataBinding.recyclerProductsRelated.adapter =
            productsGridAdapter.withLoadStateFooter(footer = LoadStateViewHolder.LoadingStateAdapter {
                productsGridAdapter.retry()
            })

    }
    fun initAdapter(){
        productsGridAdapter = RelatedAdapter(requireContext(),productData = object :
            RelatedAdapter.ProductItemListener {
            override fun itemClicked(productData: ProductsResponse.Data?) {
                details=productData!!
                setData()
                initUI()
                initSlider()
                getReviwes()
                initInformation()
                checkRelatedProduct()
            }

        })
    }

    private fun subscribeAddToCart() {
        productViewModel.addCartResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    EventBus.getDefault().postSticky(MessageEvent("cart"))
                    dismissLoading()
                    startActivity(Intent(requireContext(), CongratulitionCartActivity::class.java))

                }
                Status.LOADING -> {
                    showLoading()
                }

                Status.ERROR -> {
                    dismissLoading()
                    error(resources.getString(R.string.error), it.message.toString())

                }
            }
        })
    }

    private fun init() {
        productViewModel.navigator = this
        mViewDataBinding.viewmodel = productViewModel
    }


    private fun subscribeAddFavourit() {
        productViewModel.addFavouritResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.add_favourit),
                        Toast.LENGTH_SHORT
                    ).show()
                    details.isInWishlist = true
                    mViewDataBinding.ImgFavourit.setImageResource(R.drawable.ic_favourit)
                    dismissLoading()
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

    private fun subscriberemoveFavourit() {
        productViewModel.removeFavouritResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.remove_favourit),
                        Toast.LENGTH_SHORT
                    ).show()
                    details.isInWishlist = false
                    mViewDataBinding.ImgFavourit.setImageResource(R.drawable.ic_emptyfavourit)
                    dismissLoading()
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

    fun checkIsFavourit(postion: Boolean) {
        if (postion) {
            token?.let { it1 -> productViewModel.removeFavourit(details.id.toString(), it1) }
        } else {
            token?.let { it1 -> productViewModel.addFavourit(details.id.toString(), it1) }
        }

    }


    private fun getDataReviwes(List: List<ListReviwesResponse.Data>) {
        if(List.size>0){
            mViewDataBinding.reviwesRecyclerView.visibility = View.VISIBLE
            revwiesAdapter.setDeveloperList(List as MutableList<ListReviwesResponse.Data>)
        }else {
            mViewDataBinding.TNoReviwes.visibility = View.VISIBLE
            mViewDataBinding.reviwesRecyclerView.visibility = View.GONE
        }

    }

    private fun subscribeReviwes() {
        productViewModel.reviewResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    it.data?.data?.let { it1 -> getDataReviwes(it1) }
                }
                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        })
    }

    private fun getReviwes() {
        productViewModel.getReviwes(details.sku!!)
    }

    private fun initUI() {
        mViewDataBinding.reviwesRecyclerView.setHasFixedSize(true)
        mViewDataBinding.reviwesRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.reviwesRecyclerView.adapter = revwiesAdapter
    }


    private fun initInformation() {
        var informationAdapter = InformationAdapter(details.productCustomAttributes)
        mViewDataBinding.moreinformationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mViewDataBinding.moreinformationRecyclerView.adapter = informationAdapter

    }


    private fun getData() {
        bundle = this.requireArguments()
        details = bundle.getParcelable("item")!!

    }
    private fun setData(){
        mViewDataBinding.model = details
        data = SharedData(requireContext())
        productViewModel.produtId.value=details.id.toString()
        productViewModel.userId.value=data?.getValue(SharedData.ReturnValue.STRING, "id")
        totalPrice = details.final_price.toInt()

    }
    private fun checkRelatedProduct(){
        if(details.hasRelatedProducts){
            mViewDataBinding.TRelated.visibility=View.VISIBLE
            mViewDataBinding.recyclerProductsRelated.visibility=View.VISIBLE
        }else {
            mViewDataBinding.TRelated.visibility=View.GONE
            mViewDataBinding.recyclerProductsRelated.visibility=View.GONE
        }
    }

    private fun checkStock() {
        if (details.isSalable ==true) {
            mViewDataBinding.BtnCart.isEnabled = true
            mViewDataBinding.imgTrue.setImageResource(R.drawable.img_true)
            mViewDataBinding.Stock.text = resources.getString(R.string.in_stock)
        } else {
            mViewDataBinding.imgTrue.setImageResource(R.drawable.img_false)
            mViewDataBinding.Stock.text = resources.getString(R.string.out_stock)
            mViewDataBinding.TCart.text=resources.getString(R.string.out_stock)
            mViewDataBinding.BtnCart.isEnabled = false
            mViewDataBinding.ImgPlus.isEnabled=false

        }
    }

    private fun initSlider() {
        val productsSliderAdapter = SliderProductAdapter(details.mediaGalleryEntries)
        mViewDataBinding.sliderView.setSliderAdapter(productsSliderAdapter)
//        mViewDataBinding.sliderView.startAutoCycle();
        mViewDataBinding.sliderView.setSliderTransformAnimation(SliderAnimations.HORIZONTALFLIPTRANSFORMATION);
    }


    fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

     fun onClickAddCart() {
         mViewDataBinding.BtnCart.setOnClickListener(){
             if(!requireContext().isConnected){
                 startActivity(Intent(requireContext(), NoInternertActivity::class.java))
             }
             if (checkMinimumCart()) {
                 if (isLogin()) {
                     addAuthCart()
                 } else {
                     if (checkQutaCart()) {
                         addGustCart()
                     } else {
                         createGustCart()
                     }
                 }

             }
         }

    }



    fun checkQutaCart(): Boolean {
        quataId = data?.getValue(SharedData.ReturnValue.STRING, "quta_id")
        if (!quataId.isNullOrEmpty()) {

            return true
        }
        return false

    }

    fun addAuthCart() {
        var request = RequestAddToCartResponse(
            RequestAddToCartResponse.CartItem(
                counter,
                details.sku
            ,null)
        )
        token?.let { productViewModel.addToCart(it, request) }
    }

    private fun isLogin(): Boolean {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun createGustCart() {
        productViewModel.createCart()
    }

    private fun subscribeCreateCart() {
        productViewModel.createCartResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    quataId = it.data?.data?.id.toString()
                    data?.putValue("quta_id",quataId)
                    addGustCart()
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

    private fun subscribeaddGustCart() {
        productViewModel.addGustCarttResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    EventBus.getDefault().postSticky(MessageEvent("cart"))
                    dismissLoading()
                    startActivity(Intent(requireContext(), CongratulitionCartActivity::class.java))

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

    private fun addGustCart() {
        if (checkMinimumCart()) {
            var request = AddGustCartResponse(
                AddGustCartResponse.CartItem(
                    counter,
                    quataId,
                    details.sku
                )
            )
            productViewModel.addGustCart(request)
        }
    }

    override fun onClickAddReview() {
        if(isLogin()){
            val newDialogFragment = AddReviewFragment()
            val bundle2 = Bundle()
            bundle2.putParcelable("item", details)
            newDialogFragment.arguments = bundle2
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            newDialogFragment.show(transaction, "New_Dialog_Fragment")
        }else {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }


    }

    override fun onClickToggleReviwes() {
        var checked = toggleArrow(mViewDataBinding.btToggleReviews)
        if (checked) {
            mViewDataBinding.lytExpandReviews.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandReviews.visibility = View.GONE
        }
    }

    override fun onClickAddFavourit() {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if (!token.isNullOrEmpty()) {
            checkIsFavourit(details.isInWishlist!!)
        } else {
          startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }


    override fun onClickMore() {
        var checked = toggleArrow(mViewDataBinding.btToggleMore)
        if (checked) {
            mViewDataBinding.lytExpandMore.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandMore.visibility = View.GONE
        }
    }

    override fun onClickDescrption() {
        var checked = toggleArrow(mViewDataBinding.btToggleDescription)
        if (checked) {
            mViewDataBinding.lytExpandDescription.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandDescription.visibility = View.GONE
        }
    }

    override fun onClickMinus() {
        checkMinCart()
    }

    override fun onCLickFinish() {
        dismiss()
    }

    override fun onClickShipFirst() {
        var intent =Intent(requireContext(),PolicyActivitiy::class.java)
        intent.putExtra("link","https://cairocart.com/en/hassle-free-policies")
        startActivity(intent)

    }

    override fun onClickShipSecond() {
        var intent =Intent(requireContext(),PolicyActivitiy::class.java)
        intent.putExtra("link","https://cairocart.com/en/100-day-returning-policy")
        startActivity(intent)
    }

    override fun onClickShipThird() {
        var intent =Intent(requireContext(),PolicyActivitiy::class.java)
        intent.putExtra("link","https://cairocart.com/en/hassle-free-policies")
        startActivity(intent)
    }

    override fun onClickShipFourth() {
        var intent =Intent(requireContext(),PolicyActivitiy::class.java)
        intent.putExtra("link","https://cairocart.com/en/hassle-free-policies")
        startActivity(intent)
    }

    override fun onClickShipFifth() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + "01099944331")
        startActivity(dialIntent)
    }

    override fun onClickPlus() {
            counter++
            mViewDataBinding.TQuantity.text = counter.toString()
    }

    @SuppressLint("SetTextI18n")
    fun checkMaxCart() {
        val countValue: Int = details.cartMaxAllowed!!
        val defult = mViewDataBinding.TQuantity.text.toString().toInt()
        if (countValue > defult) {
            counter++
            mViewDataBinding.TQuantity.text = counter.toString()
            var total = totalPrice * counter
//            mViewDataBinding.TPrice.text = total.toString() + resources.getString(R.string.currency)
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.max_cart) + " " + details.cartMaxAllowed,
                Toast.LENGTH_SHORT
            ).show()
            mViewDataBinding.ImgPlus.isEnabled = false
        }

    }

    @SuppressLint("SetTextI18n")
    fun checkMinCart() {
        if (counter > 1) {
            mViewDataBinding.ImgPlus.isEnabled = true
            counter--
            mViewDataBinding.TQuantity.text = counter.toString()
            var total = totalPrice * counter
//            mViewDataBinding.TPrice.text = total.toString() + resources.getString(R.string.currency)
        }
    }

    fun checkMinimumCart(): Boolean {
        if (mViewDataBinding.TQuantity.text.toString().toInt() >= details.cartMinAllowed!!) {
            return true
        }
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.min_cart) + " " + details.cartMinAllowed,
            Toast.LENGTH_SHORT
        ).show()
        return false
    }


    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        if(messsg.Message.equals("login")){
            token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        }
    };
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }
}