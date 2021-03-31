package com.cairocartt.ui.resultfilter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.FilterResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.productsbyId.ProductByIdNavigator

class ResultFitertionViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<ProductByIdNavigator>(dataCenterManager) {
    var checkChanges = MutableLiveData<Boolean>()
    var Lang = MutableLiveData<String>()
    var userId = MutableLiveData<String>()
    var category_Id = MutableLiveData<String>()
    var brand_Id = MutableLiveData<String>()
    var search_term = MutableLiveData<String>()

    var filter = MutableLiveData<ArrayList<FilterResponse.Data.Value>>()


    var listData = Pager(PagingConfig(pageSize = 10)) {
        ProductsPagination(
            dataCenterManager,
            search_term.value.toString(),
            brand_Id.value.toString(),
            category_Id.value.toString(),
            filter.value,
            userId.value.toString(),
            Lang.value.toString()
        )
    }.flow

}

class ProductsPagination constructor(
    dataCenterManager: DataCenterManager,
    search_term: String?,
    brand_id: String?,
    cat_id: String?,
    list: MutableList<FilterResponse.Data.Value>?,
    userId: String,
    Lang: String
) :
    PagingSource<Int, ProductsResponse.Data>() {
    var cat_id: String? = String()
    var brand_id: String? = String()
    var search_term: String? = String()

    var list: MutableList<FilterResponse.Data.Value>?
    var dataCenterManager: DataCenterManager
    var lang: String
    var userId: String

    init {
        this.search_term = search_term
        this.brand_id = brand_id
        this.cat_id = cat_id
        this.lang = Lang
        this.list = list
        this.dataCenterManager = dataCenterManager
        this.userId = userId
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponse.Data> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            var hashMap = HashMap<String, String>()
            hashMap.put("searchCriteria[currentPage]", currentLoadingPageKey.toString())
            hashMap.put("searchCriteria[pageSize]", "10")

            if (!cat_id.isNullOrEmpty()) {
                hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "category_id")
                cat_id?.let {
                    hashMap.put(
                        "searchCriteria[filterGroups][0][filters][0][value]",
                        it
                    )
                }
            }
            if (!brand_id.isNullOrEmpty()) {
                hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "manufacturer")
                brand_id?.let {
                    hashMap.put(
                        "searchCriteria[filterGroups][0][filters][0][value]",
                        it
                    )
                }
            }

            list?.forEachIndexed { index, value ->
              var n_index=index
                list?.get(index)?.field?.let {
                    hashMap.put(
                        "searchCriteria[filterGroups][0][filters][$n_index][field]",
                        it
                    )
                }
                list?.get(index)?.value?.let {
                    hashMap.put(
                        "searchCriteria[filterGroups][0][filters][$n_index][value]",
                        it
                    )
                }
            }


//            if(!search_term.isNullOrEmpty()){
//                hashMap.put("searchCriteria[filterGroups][0][filters][1][field]", "search_term")
//                hashMap.put("searchCriteria[filterGroups][0][filters][1][value]", "aaa")
//                hashMap.put("searchCriteria[requestName]", "quick_search_container")
//
//            }


            val response = dataCenterManager.getProductsById(
                lang, "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl", userId, hashMap,
                BaseActivity.token
            )

            val responseData = mutableListOf<ProductsResponse.Data>()
            val data = response.body()!!.data
            responseData.addAll(data)
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            if (data.size > 0) {
                return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            } else
                return LoadResult.Error(IllegalAccessException())
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}

