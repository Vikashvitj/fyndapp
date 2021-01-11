package com.demoapp.fyndapp.data.network

import com.demoapp.fyndapp.util.ApiErrorModel
import com.demoapp.fyndapp.util.AppLog
import com.demoapp.fyndapp.util.NetworkUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response


object ApiRepository {

    fun <R> callApi(
        request: Deferred<Response<R>>,
        apiCallBack: ApiCallback<R>?
    ) {
        AppLog.d("Current UTC Time : " + System.currentTimeMillis().toString())
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    apiCallBack?.onSuccess(response.body())
                } else {
                    val errorModel = NetworkUtils.getApiErrorList(response)
                    apiCallBack?.onError(errorModel)

                }
            } catch (e: Exception) {
                AppLog.printStackTrace(e)
                apiCallBack?.onException(e)
            }
        }
    }

}

interface ApiCallback<T> {
    fun onException(error: Throwable)
    fun onError(errorModel: ApiErrorModel)
    fun onSuccess(t: T?)
}