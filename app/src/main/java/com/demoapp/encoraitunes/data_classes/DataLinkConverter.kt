package com.demoapp.encoraitunes.data_classes

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object DataLinkConverter {
    val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun arrayListToJson(list: List<Link>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToArrayList(jsonData: String?): List<Link>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<Link>?>() {}.type)
    }
}
