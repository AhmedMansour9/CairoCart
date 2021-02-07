package com.cairocartt.ui.ordersuccess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.cairocartt.R
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import kotlinx.android.synthetic.main.activity_order_success.*

class OrderSuccessActivity : AppCompatActivity() {
    var Id:String?= String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_order_success)
        Id=intent.getStringExtra("id")
        T_OrderId.text=resources.getString(R.string.orderid)+" "+Id

    }


    fun Btn_finish(view: View) {
        val intent= Intent(this, BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this, BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}