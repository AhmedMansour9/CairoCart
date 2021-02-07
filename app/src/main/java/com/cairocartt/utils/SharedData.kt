package com.cairocartt.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedData @SuppressLint("CommitPrefEdits") constructor(context: Context) {
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        preferences = context.getSharedPreferences("shader", Context.MODE_PRIVATE)
        editor = preferences?.edit()
    }


    @Suppress("UNCHECKED_CAST")
    fun <V> getValue(returnValue: ReturnValue?, key: String?): V? {
        var vs: V? = null
        when (returnValue) {
            ReturnValue.INT -> vs = (preferences!!.getInt(key, 0)) as V
            ReturnValue.STRING -> vs = (preferences!!.getString(key, "")) as V
            ReturnValue.LONG -> vs = (preferences!!.getLong(key, 0L)) as V
            ReturnValue.BOOLEAN -> vs = (preferences!!.getBoolean(key, false)) as V
        }
        return vs
    }

    fun <V> putValue(key: String?, value: V) {
        if (value is Int) {
            val i = value as Int
            editor?.putInt(key, i)
            editor?.apply()
        } else if (value is String) {
            val i = value as String
            Log.d("TTTTTOJKEN", "putValue: " + i)
            editor?.putString(key, i)
            editor?.apply()

        } else if (value is Float) {
            val x = value as Float
            editor?.putFloat(key, x)
            editor?.apply()
        } else if (value is Boolean) {
            val x = value as Boolean
            editor?.putBoolean(key, x)
            editor?.apply()
        } else if (value is Long) {
            val x = value as Long
            editor?.putLong(key, x)
            editor?.apply()
        }
    }

    fun clear() {
        editor!!.clear().apply()
    }


    enum class ReturnValue {
        INT, STRING, LONG, BOOLEAN
    }

}