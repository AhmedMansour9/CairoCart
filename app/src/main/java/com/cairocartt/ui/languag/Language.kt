package com.cutz.ui.languag

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.cairocartt.R
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.SharedData
import com.yariksoffice.lingver.Lingver

class Language : AppCompatActivity() {

    private var data: SharedData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_language)

        data = SharedData(this)
        val v = window.decorView
        v.setBackgroundResource(android.R.color.transparent)


    }

    fun Btn_Arabic(view: View) {
        data?.putValue("Lann","ar")
        Lingver.getInstance().setLocale(this, "ar", "")
        val intent = Intent(this, BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    fun Btn_English(view: View) {
        data?.putValue("Lann","en")
        Lingver.getInstance().setLocale(this,"en", "")
        val intent = Intent(this, BottomNavigateFragment::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }
}