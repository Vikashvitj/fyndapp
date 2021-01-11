package com.demoapp.fyndapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response
import java.io.IOException


object NetworkUtils {

    /**
     * @param response is the response of api in case of not success
     * @return returns a model (ApiErrorModel) having errorCode, message and list of errors
     */
    fun getApiErrorList(response: Response<*>): ApiErrorModel {

        val errorArray: ArrayList<KeyValuePair> = ArrayList()
        val apiErrorModel = ApiErrorModel("", "", errorArray)

        try {
            when (response.code()) {
                AppConstant.HTTP_STATUS_UNAUTHORIZED, AppConstant.HTTP_STATUS_BAD_REQUEST, AppConstant.HTTP_STATUS_FORBIDDEN -> {
                    val gSon = Gson()
                    val adapter =
                        gSon.getAdapter(
                            ErrorResponse::class.java
                        )

                    val errorBody = response.errorBody()?.string()
                    if (null != errorBody) {
                        val errorParser = adapter.fromJson(errorBody)
                        apiErrorModel.message = errorParser.message
                        apiErrorModel.errorCode = errorParser.error_code
                    }
                    val errorsList = parseErrorList(errorBody)
                    if (apiErrorModel.message.isNullOrEmpty() && errorsList.isNullOrEmpty().not()) {
                        apiErrorModel.errorList = errorsList
                        apiErrorModel.message = errorsList?.get(0)?.value
                    }
                }

                AppConstant.HTTP_STATUS_UN_PROCESSABLE_ENTITY -> {
                    val error = response.errorBody()?.string()

                    if (null != response.errorBody()) {
                        val errors = parseErrorList(error)
                        apiErrorModel.errorList = errors
                        if (error.isNullOrEmpty().not()) {
                            apiErrorModel.message = errors?.get(0)?.value
                        }

                    } else {
                        apiErrorModel.message = response.message()
                    }
                }
                else -> {
                    apiErrorModel.message = response.message()
                }
            }
        } catch (e: IOException) {
            apiErrorModel.message = response.message()
            e.printStackTrace()
        }
        return apiErrorModel
    }

    private fun parseErrorList(errorBody: String?): ArrayList<KeyValuePair>? {
        val errorArray = ArrayList<KeyValuePair>()
        val array = Gson().fromJson(
            errorBody,
            LinkedTreeMap::class.java
        ).toList()
        if (array.isNotEmpty()) {
            for ((first, second) in array) {
                errorArray.add(
                    KeyValuePair(
                        first.toString(),
                        second.toString()
                    )
                )
            }
        }
        return errorArray
    }

    /**
     * Connection Available or not
     */
    @SuppressLint("MissingPermission")
    fun isConnectionAvailable(context: Context): Boolean {
        val hasNet: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        hasNet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_VPN
            ) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_BLUETOOTH
            ))
        } else {
            connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
        }
        return hasNet
    }
}

@Keep
data class ApiErrorModel(
    var errorCode: String?,
    var message: String?,
    var errorList: ArrayList<KeyValuePair>?
)

@Keep
data class KeyValuePair(
    val key: String,
    val value: String
)

@Keep
class ErrorResponse {
    var error_code: String? = null
    var message: String? = null
}