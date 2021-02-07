package com.cairocartt.ui.resultsearch

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.productsbyId.ProductByIdNavigator

class SearchResultViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<ProductByIdNavigator>(dataCenterManager) {
    var checkChanges = MutableLiveData<Boolean>()
    var Lang = MutableLiveData<String>()
    var userId = MutableLiveData<String>()
    var category_Id = MutableLiveData<String>()
    var search_term = MutableLiveData<String>()

    var listData = Pager(PagingConfig(pageSize = 10)) {
        ProductsPagination(
            dataCenterManager,
            search_term.value.toString(),
            category_Id.value.toString(),
            userId.value.toString(),
            Lang.value.toString())
    }.flow
}

class ProductsPagination constructor(
    dataCenterManager: DataCenterManager, search_term:String?, categoryId: String, userId: String, Lang: String
) :
    PagingSource<Int, ProductsResponse.Data>() {
    var cat_id:String?= String()
    var search_term :String?= String()

    var dataCenterManager: DataCenterManager
    var lang: String
    var userId: String

    init {
        this.search_term = search_term
        this.cat_id = categoryId
        this.lang = Lang
        this.dataCenterManager = dataCenterManager
        this.userId = userId
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponse.Data> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            var hashMap = HashMap<String, String>()
            cat_id?.let { hashMap.put("searchCriteria[filterGroups][0][filters][0][value]", it) }
            hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "category_id")
            hashMap.put("searchCriteria[currentPage]", currentLoadingPageKey.toString())
            hashMap.put("searchCriteria[pageSize]", "10")
//            if(!brand_id.isNullOrEmpty()){
//                hashMap.put("searchCriteria[filterGroups][0][filters][1][field]", "manufacturer")
//                brand_id?.let { hashMap.put("searchCriteria[filterGroups][0][filters][1][value]", it) }
//            }
            Log.e("search_term",search_term.toString())

                hashMap.put("searchCriteria[filterGroups][0][filters][1][field]", "search_term")
                hashMap.put("searchCriteria[filterGroups][0][filters][1][value]", search_term.toString())
                hashMap.put("searchCriteria[requestName]", "quick_search_container")

//            if(!min_Price.isNullOrEmpty() && min_Price!=null ){
//                Log.e("min_Price",min_Price.toString())
//
//                hashMap.put("searchCriteria[filterGroups][0][filters][2][field]", "price")
//                min_Price?.let { hashMap.put("searchCriteria[filterGroups][0][filters][2][value]", it) }
//                hashMap.put("searchCriteria[filterGroups][0][filters][2][condition_type]", "moreq")
//            }
//
//
//            if(!max_Price.isNullOrEmpty()  ) {
//                hashMap.put("searchCriteria[filterGroups][0][filters][3][field]", "price")
//                max_Price?.let { hashMap.put("searchCriteria[filterGroups][0][filters][3][value]", it) }
//                hashMap.put("searchCriteria[filterGroups][0][filters][3][condition_type]", "lteq")
//            }


            val response = dataCenterManager.searchProducts(
                lang, "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl", userId, hashMap ,
                BaseActivity.token )

            val responseData = mutableListOf<ProductsResponse.Data>()
            val data = response.body()!!.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            if (data.size>0){
                return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1))
            }else
                return LoadResult.Error(IllegalAccessException())
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}