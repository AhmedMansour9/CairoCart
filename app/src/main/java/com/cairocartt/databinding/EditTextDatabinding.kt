package com.cairocartt.databinding

import android.graphics.Paint
import android.os.Build
import android.text.Html
import android.util.Patterns
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cairocartt.R
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern


class EditTextDatabinding {
    companion object {
        @BindingAdapter("emailVaild")
        @JvmStatic
        fun emailVaid(editText: TextInputLayout, msg: String) {
            editText.editText?.doAfterTextChanged {
                if (!Patterns.EMAIL_ADDRESS.matcher(editText.editText?.text.toString()).matches()) {
                    editText.error = msg
                } else {
                    editText.error = null
                }
            }

        }

        @BindingAdapter("passwordVaild")
        @JvmStatic
        fun passwordValid(editText: TextInputLayout, msg: String) {
            val passwordRegex = "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}"               //at least 8 characters
//                    "$"
            editText.editText?.doAfterTextChanged {
                if (!Pattern.compile(passwordRegex).matcher(editText.editText?.text.toString())
                        .matches()
                ) {
                    editText.error = msg
                } else {
                    editText.error = null
                }
            }
        }

        @BindingAdapter("passwordloginValid")
        @JvmStatic
        fun passwordloginValid(editText: TextInputLayout, msg: String) {
            val passwordRegex = "^" + "(?=\\S+$)" + ".{7,}"

            editText.editText?.doAfterTextChanged {
                if (!Pattern.compile(passwordRegex).matcher(editText.editText?.text.toString())
                        .matches()
                ) {
                    editText.error = msg
                } else {
                    editText.error = null
                }
            }
        }

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
                Glide.with(view.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform()
                    .into(view)
        }

        @BindingAdapter("imagedrawableUrl")
        @JvmStatic
        fun loaddrawableImage(view: ImageView, url: Int) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }


        @BindingAdapter("checkFavourit")
        @JvmStatic
        fun checkFavourit(view: ImageView, status: Boolean) {
            if (status==false) {
                view.setImageResource(R.drawable.ic_emptyfavourit)
            }else{
                view.setImageResource(R.drawable.ic_favourit)

            }
        }

        @BindingAdapter("descrption")
        @JvmStatic
        fun descrption(view: TextView, Data: String?) {
            if(!Data.isNullOrEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.setText(
                        Html.fromHtml(
                            Data,
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    )
                } else {
                    view.setText(HtmlCompat.fromHtml(Data!!, 0));

                }
            }

        }


        @BindingAdapter(value = ["bind:BeforePrice", "bind:FinalPrice"], requireAll = false)
        @JvmStatic
        fun textPrice(textPrice: TextView, BeforePrice: String, FinalPrice: String) {

         if(BeforePrice.toDouble()>FinalPrice.toDouble()){
             textPrice.isVisible=true
             textPrice.text=BeforePrice+" "+textPrice.resources.getString(R.string.currency)
             textPrice.setPaintFlags(textPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
         }

        }


        @BindingAdapter(value = ["bind:BeforePrice2", "bind:FinalPrice2"], requireAll = false)
        @JvmStatic
        fun textSalePrice(textPrice2: TextView?, BeforePrice: String?, FinalPrice: String?) {

            if(!BeforePrice.isNullOrEmpty() && !FinalPrice.isNullOrEmpty() ){
                if(BeforePrice.toDouble()> FinalPrice.toDouble()){
                    textPrice2?.isVisible=true
                    textPrice2?.text=BeforePrice+" "+textPrice2?.resources?.getString(R.string.currency)
                    textPrice2?.setPaintFlags(textPrice2.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                }
            }
        }


        @BindingAdapter("raitingBar")
        @JvmStatic
        fun raitingbar(view: RatingBar?, rating: Int) {
            if(view!=null){
                val rate = rating.toFloat()
                view.rating = rate
            }
        }



    }




    }
