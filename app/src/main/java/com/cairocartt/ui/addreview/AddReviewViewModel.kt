package com.cairocartt.ui.addreview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.AddReviewResponse
import com.cairocartt.data.remote.model.RequestAddReviewResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.inject.Singleton
import javax.security.auth.callback.Callback


@Singleton
class
AddReviewViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<AddReviewNavigator>(dataCenterManager) {


    var addReview: RequestAddReviewResponse.Review = RequestAddReviewResponse.Review()
    var revieww: RequestAddReviewResponse = RequestAddReviewResponse()

    var reviewqueality: Float = 1f
    var quality: String = "Quality"

    var reviewprice: Float = 1f
    var price: String = "Price"

    var reviewvalue: Float = 1f
    var value: String = "Value"

    private val _reviewResponse = MutableLiveData<Resource<AddReviewResponse>>()
    val reviewResponse: LiveData<Resource<AddReviewResponse>>
        get() = _reviewResponse

    fun fillListRates(): MutableList<RequestAddReviewResponse.Review.Rating> {
        var newListRating: MutableList<RequestAddReviewResponse.Review.Rating>? = mutableListOf()

        var Rates = RequestAddReviewResponse.Review.Rating(quality, reviewqueality.toInt())
        newListRating?.add(Rates)
        var Rates2 = RequestAddReviewResponse.Review.Rating(price, reviewprice.toInt())
        newListRating?.add(Rates2)

        var Rates3 = RequestAddReviewResponse.Review.Rating(value, reviewvalue.toInt())
        newListRating?.add(Rates3)
        return newListRating!!
    }


    fun raddReview(token: String) {


        addReview.ratings = fillListRates()
        revieww?.review = addReview

        if (!revieww.empty()) {
            _reviewResponse.postValue(Resource.loading(null))
            dataCenterManager.addReviewProduct("Bearer $token", revieww,BaseActivity.token)
                .enqueue(object : Callback, retrofit2.Callback<AddReviewResponse> {
                    override fun onResponse(
                        call: Call<AddReviewResponse>,
                        response: Response<AddReviewResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status?.code == 200) {
                                _reviewResponse.postValue(Resource.success(response.body()))

                            } else {
                                _reviewResponse.postValue(
                                    Resource.error(
                                        response.body()?.status?.message.toString(),
                                        null
                                    )
                                )
                            }
                        } else
                            _reviewResponse.postValue(Resource.error(response.message(), null))


                    }

                    override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                        _reviewResponse.postValue(Resource.error(t.message.toString(), null))
                    }
                })
        }
    }


}