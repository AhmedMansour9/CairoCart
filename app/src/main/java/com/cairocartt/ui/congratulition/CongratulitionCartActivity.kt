package com.cairocartt.ui.congratulition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.cairocartt.R
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment

class CongratulitionCartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_congratulition)
    }

    fun Btn_Cart(view: View) {
     val intent=Intent(this,BottomNavigateFragment::class.java)
        intent.putExtra("data","cart")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun Btn_finish(view: View) {
        finish()
    }
}