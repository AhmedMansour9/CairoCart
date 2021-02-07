package com.cairocartt.ui.wishlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.ListFavouritResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.myaccount.MyAccountNavigator
import java.util.*
import kotlin.collections.HashMap

class WishListViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<MyAccountNavigator>(dataCenterManager) {
    var Lang = MutableLiveData<String>()
    var token  = MutableLiveData<String>()

    var listData = Pager(PagingConfig(pageSize = 10)) {
        ProductsPagination(dataCenterManager,token.value.toString())
    }.flow
}
class ProductsPagination constructor(
    dataCenterManager: DataCenterManager, token:String
) :
    PagingSource<Int, ListFavouritResponse.Data>() {
    var dataCenterManager: DataCenterManager
    var token :String

    init {
        this.dataCenterManager = dataCenterManager
        this.token=token
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListFavouritResponse.Data> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            var hashMap = HashMap<String, String>()


            val response = dataCenterManager.getWishList(Locale.getDefault().getLanguage()+"/","Bearer $token",
                BaseActivity.token)

            val responseData = mutableListOf<ListFavouritResponse.Data>()
            val data = response.body()!!.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}