package com.cairocart.adapter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.cairocart.R
import com.cairocart.databinding.ViewTreeItemBinding
import com.cairocart.data.remote.model.CatModel
import com.cairocart.data.remote.model.MessageEvent
import com.cairocart.utils.setMargins
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.greenrobot.eventbus.EventBus
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

        binding.expandIv.clicks()
            .debounce(250)
            .onEach { itemClickedListener?.invoke() }
            .launchIn(scope)

//        clicks()
//            .debounce(250)
//            .onEach { itemClickedListener?.invoke() }
//            .launchIn(scope)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope?.cancel()
    }


    @ModelProp
    fun setItemData(CatModel: CatModel) {
        binding.root.setOnClickListener(){
            EventBus.getDefault().postSticky(MessageEvent(CatModel))
        }
        binding.titleTv.text = CatModel.name
        setMargins(CatModel.level * 16)
        binding.expandIv.setImageResource(

                if (CatModel.isExpanded) {
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
