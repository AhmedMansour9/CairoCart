package com.cairocartt.ui.bottomnavigate

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.BottomFragmentBinding
import com.cairocartt.ui.cart.CartViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_fragment.*
import kotlinx.android.synthetic.main.fragment_create_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class BottomNavigateFragment : BaseActivity<BottomFragmentBinding>() {


    override var idLayoutRes: Int = R.layout.bottom_fragment
    private lateinit var navController: NavController
    private var data: SharedData? = null
         var token: String? = String()

    private var quta_id: String? = String()
    var Lang: String? = String()
    companion object {
        var notificationType: String? = String()
        var id: String? = String()
        var title: String? = String()
    }
    val mViewModel: CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = SharedData(this)
        init()
        setSupportActionBar(toolbar)
        changeStatusBarColor()
        setupNavigationView()
        getData()
        getLang()
        getCart()
        subscribeCart()


    }

    private fun init() {
        token = data?.getValue(SharedData.ReturnValue.STRING, "access_firebase")

    }


    private fun getData() {
        notificationType=intent.getStringExtra("notificationType")
        if(!notificationType.isNullOrEmpty()){
            id =intent.getStringExtra("id")
            title=intent.getStringExtra("name")
        }

        var message=intent.getStringExtra("data")
        if(!message.isNullOrEmpty()){
            Navigation.findNavController(this,R.id.fragment)
                .navigate(R.id.cartFragment);
        }
    }

    private fun setupNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupWithNavController(bottomNavigationView, navHostFragment.navController)
//        bottomNavigationView.setOnNavigationItemReselectedListener(){
//        }
    }

    private fun preventRecreate() = bottomNavigationView.setOnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.T_Categories -> Navigation.findNavController(this, R.id.fragment)
                .navigate(R.id.T_Categories)

            R.id.productsById -> Navigation.findNavController(this, R.id.fragment)
                .navigate(R.id.productsById)

            else -> Navigation.findNavController(this, R.id.fragment).navigate(R.id.homeFragment)
        }
        return@setOnNavigationItemSelectedListener true
    }


    private fun changeStatusBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            Exit()
        }
        else {
            super.onBackPressed()
        }
    }



    fun Exit(){
        var dialog= AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.exit_message))
            .setPositiveButton(resources.getString(R.string.yes), DialogInterface.OnClickListener {
                    dialog, which ->  finish()
            })
            .setNegativeButton(resources.getString(R.string.no), DialogInterface.OnClickListener { dialog, which ->
                return@OnClickListener
            })
            .show()
        val buttonPositive: Button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val buttonNegative: Button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

    }



    fun getCart() {
        quta_id=data?.getValue(SharedData.ReturnValue.STRING, "quta_id")

       var accessToken:String?  = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if(isLogin()){
            mViewModel.getCart(accessToken,null)
        }
        else if (!quta_id.isNullOrEmpty()){
            mViewModel.getCart(null,quta_id)
        } else {
            bottomNavigationView.getOrCreateBadge(R.id.cartFragment).setVisible(false)
        }
    }

    private fun isLogin(): Boolean {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }
    fun checkToken(): Boolean {
        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }


    private fun subscribeCart() {
        mViewModel.cartResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> setCounterCart(it1) }

                }
                Status.ERROR -> {
                   hideCounterart()
                }
            }
        })
    }

    private fun setCounterCart(list: ListCartResponse) {
        if(list.data!=null) {
            if (list.data!!.itemsCount > 0) {
                bottomNavigationView.getOrCreateBadge(R.id.cartFragment).setVisible(true)
                bottomNavigationView.getOrCreateBadge(R.id.cartFragment).number =
                    list.data!!.itemsCount!!
            } else {
                bottomNavigationView.getOrCreateBadge(R.id.cartFragment).setVisible(false)
            }
        }
    }
    fun hideCounterart(){
        bottomNavigationView.getOrCreateBadge(R.id.cartFragment).setVisible(false)
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        Log.d("IGNORE", "Logging view to curb warnings: $messsg")
       if(messsg.Message.equals("cart") || messsg.Message.equals("login"))
       {
           getCart()
       }

    };

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }


    }


    override fun onResume() {
        super.onResume()
    }

    private fun getLang() {
        Lang = data?.getValue(SharedData.ReturnValue.STRING, "Lann")
        if (Lang.isNullOrEmpty()) {
            Lang="en"
        }
    }
}