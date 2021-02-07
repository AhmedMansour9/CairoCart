package com.cairocartt.adapter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.cairocartt.R
import com.cairocartt.databinding.ViewTreeItemBinding
import com.cairocartt.data.remote.model.CatModel
import com.cairocartt.utils.setMargins
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks


@ExperimentalCoroutinesApi
@FlowPreview
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CategoriesAdapter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTreeItemBinding =
        ViewTreeItemBinding.inflate(LayoutInflater.from(context), this)

    private var viewScope: CoroutineScope? = null

    var itemClickedListener: (() -> Unit)? = null
        @CallbackProp set

    init {
// setBackgroundColorRes(R.color.colorPrimaryDark)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        viewScope = scope

        Log.d(TAG, "onAttachedToWindow: ")

        clicks()
            .debounce(250)
            .onEach { itemClickedListener?.invoke() }
            .launchIn(scope)


    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        viewScope?.cancel()
//    }

    private   val TAG = "CategoriesAdapter"
    @ModelProp
    fun setItemData(catModel: CatModel) {
        binding.titleTv.text = catModel.name
        setMargins(catModel.level * 16)
        Log.d(TAG, "setItemData: "+catModel.name+":"+catModel.notChild +" --- "+catModel.isExpanded)
        binding.expandIv.setImageResource(
                if (catModel.isExpanded) {
                    R.drawable.ic_baseline_remove_circle_24
                } else {
                    R.drawable.ic_baseline_add_circle_24
                }

        )

        binding.expandIv.setImageResource(
            if (catModel.notChild) {
                R.drawable.ic_baseline_remove_circle_24
            } else {
                R.drawable.ic_baseline_add_circle_24
            }
     )


    }
    public interface ItemListener {
        fun itemClicked( node: CatModel);
    }

    @ColorInt
    fun darkenColor(@ColorInt color: Int, level: Float): Int {
        return ColorUtils.blendARGB(color, Color.BLACK, level)
    }
}
