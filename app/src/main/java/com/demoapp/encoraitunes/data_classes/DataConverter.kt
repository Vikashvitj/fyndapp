package com.demoapp.encoraitunes.data_classes

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object DataConverter {
    private val gson = Gson()
    @TypeConverter
    @JvmStatic
    fun arrayListToJson(list: List<ImImage>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToArrayList(jsonData: String?): List<ImImage>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<ImImage>?>() {}.type)
    }
}
