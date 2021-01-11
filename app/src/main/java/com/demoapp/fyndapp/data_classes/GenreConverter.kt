package com.demoapp.fyndapp.data_classes

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object GenreConverter {
    private val gson = Gson()
    @TypeConverter
    @JvmStatic
    fun arrayListToJson(list: ArrayList<Int>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToArrayList(jsonData: String?): ArrayList<Int>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<ArrayList<Int>?>() {}.type)
    }
}
