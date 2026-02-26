package ir.unalzo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

val Context.sharedPrefs: SharedPreferences
    get() = getSharedPreferences(
        "main",
        Context.MODE_PRIVATE
    )

inline operator fun <reified T> SharedPreferences.get(key: String): T {
    return when(T::class){
        String::class -> getString(key,null)
        Int::class -> getInt(key,0)
        Boolean::class -> getBoolean(key,false)
        else -> error("not supported")
    } as T
}
inline operator fun <reified T> SharedPreferences.set(key: String,value:T) {
     edit {
         when(T::class){
             String::class -> putString(key,value as String)
             Int::class -> putInt(key,value as Int)
             Boolean::class -> putBoolean(key,value as Boolean)
             else -> error("not supported")
         }
     }
}