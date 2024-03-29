package com.cairocartt.ui.productsbyId

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.FilterModel
import com.cairocartt.data.remote.model.FilterResponse
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import com.cairocartt.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.inject.Singleton
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

@Singleton
class ProductsByIdViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<ProductByIdNavigator>(dataCenterManager) {

    var filter = MutableLiveData<ArrayList<FilterResponse.Data.Value>>()




    var checkChanges = MutableLiveData<Boolean>()
    var Lang = MutableLiveData<String>()
    var userId = MutableLiveData<String>()
    var category_Id = MutableLiveData<String>()

    val changeId: LiveData<String>
        get() = category_Id

    init {
        if (!changeId.equals(category_Id)) {
            checkChanges.value = true
        }

    }

    private val _filtersResponse = SingleLiveEvent<Resource<FilterResponse>>(null)
    val filtersResponse: SingleLiveEvent<Resource<FilterResponse>>
        get() = _filtersResponse

    fun getFilters() {
        var hashMap = HashMap<String, String>()
        hashMap.put("searchCriteria[filterGroups][0][filters][0][value]",category_Id.value.toString())
        hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "category_id")
        _filtersResponse.postValue(Resource.loading(null))
        dataCenterManager.fetchFilterData(Lang.value.toString() ,hashMap)
            .enqueue(object :
                Callback, retrofit2.Callback<FilterResponse> {
                override fun onResponse(
                    call: Call<FilterResponse>,
                    response: Response<FilterResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _filtersResponse.postValue(Resource.success(response.body()))
                        } else {
                            _filtersResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.code.toString(),
                                    null
                                )
                            )
                        }
                    } else
                        _filtersResponse.postValue(Resource.error(response.message(), null))
                }
                override fun onFailure(call: Call<FilterResponse>, t: Throwable) {
                    _filtersResponse.postValue(Resource.error(t.message.toString(), null))
                }
            })
    }

    var listData = Pager(PagingConfig(pageSize = 10)) {
        ProductsPagination(
            dataCenterManager,
            category_Id.value.toString(),
            userId.value.toString(),
            Lang.value.toString()
        )
    }.flow

}

class ProductsPagination constructor(
    dataCenterManager: DataCenterManager, categoryId: String, userId: String, Lang: String
) :
    PagingSource<Int, ProductsResponse.Data>() {
    var cat_id = categoryId
    var dataCenterManager: DataCenterManager
    var lang: String
    var userId: String

    init {
        this.cat_id = categoryId
        this.lang = Lang
        this.dataCenterManager = dataCenterManager
        this.userId = userId
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponse.Data> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            var hashMap = HashMap<String, String>()
            hashMap.put("searchCriteria[filterGroups][0][filters][0][value]", cat_id)
            hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "category_id")
            hashMap.put("searchCriteria[currentPage]", currentLoadingPageKey.toString())
            hashMap.put("searchCriteria[pageSize]", "10")
            val response = dataCenterManager.getProductsById(
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


//val listData = Pager(PagingConfig(pageSize = 10)) {
//    ProductsPagination(dataCenterManager)
//}.flow.cachedIn(viewModelScope)


//private val _productsResponse = MutableLiveData<Resource<List<ProductsByIdResponse.Data?>?>>()
//val productResponse: LiveData<Resource<List<ProductsByIdResponse.Data?>?>>
//    get() = _productsResponse
//
////    init {
////        if (productResponse.value == null)
////            getProducts()
////    }
//
//fun getProductsById(lang: String, catId: String,Page:String) {
//    var hashMap = HashMap<String, String>()
//    hashMap.put("searchCriteria[filterGroups][0][filters][0][value]", catId)
//    hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "category_id")
//    hashMap.put("searchCriteria[currentPage]", Page)
//    hashMap.put("searchCriteria[pageSize]", "10")
//
//    viewModelScope.launch {
//        _productsResponse.postValue(Resource.loading(null))
//        dataCenterManager.getProductsById(lang, hashMap).let {
//            if (it.isSuccessful && it.code() == 200) {
//                val data = it.body()?.data
//                _productsResponse.postValue(Resource.success(data))
//            } else _productsResponse.postValue(Resource.error(it.message(), null))
//        }
//    }
//}