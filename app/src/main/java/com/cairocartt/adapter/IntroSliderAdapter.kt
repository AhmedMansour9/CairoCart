package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.data.remote.model.IntroSlide
import com.cairocartt.databinding.ItemOnboardingBinding

class IntroSliderAdapter (private val introSlides: List<IntroSlide>)
    : RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>(){
    //for adding text to speech listener in the onboarding fragment
    var onTextPassed: ((textView: TextView) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.binding.onboardingPage = (introSlides[position])

    }

    inner class IntroSlideViewHolder( var binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}