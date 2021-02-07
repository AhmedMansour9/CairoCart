package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.databinding.RowErrorBinding

class LoadStateViewHolder (private val binding:RowErrorBinding,retry:()->Unit):RecyclerView.ViewHolder(binding.root){

     init {
//         binding.loadStateRetry.setOnClickListener{
//             retry.invoke()
//         }
     }
    fun bind(loadStat:LoadState){
        if (loadStat is LoadState.Error){
         binding.loadStateErrorMessage.text=loadStat.error.localizedMessage
        }

        binding.loadStateProgress.isVisible=loadStat is LoadState.Loading
//        binding.loadStateRetry.isVisible=loadStat !is LoadState.Loading
        binding.loadStateErrorMessage.isVisible=loadStat !is LoadState.Loading
    }

    companion object{
        fun create(parent:ViewGroup,retry: () -> Unit):LoadStateViewHolder{
            val view=RowErrorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LoadStateViewHolder(view,retry)


        }


    }


    class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {
        override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
            return LoadStateViewHolder.create(parent, retry)
        }
    }



}