package com.aplikasi.siabsis.pref

import android.content.Context
import com.google.firebase.auth.FirebaseUser

internal class UserPreference (context : Context) {

//    tvLatitude.text = "Latitude\n${list[0].latitude}"
//    tvLongitude.text = "Longitude\n${list[0].longitude}"
//    tvCountryName.text = "Country Name\n${list[0].countryName}"
//    tvLocality.text = "Locality\n${list[0].locality}"
//    tvAddress.text = "Address\n${list[0].getAddressLine(0)}"

    companion object {
        const val EMAIL = "email"
        const val NAMA = "nama"
        const val LATITUDE = "lat"
        const val LONGITUDE = "long"
        const val COUNTRY_NAME = "country"
        const val LOCALITY = "locality"
        const val ADDRESS = "address"
        private const val IS_LOGIN = "is login"
    }

    private val preferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun setUser(value: String?) {
        editor.putString(EMAIL, value)
        editor.apply()
    }

    fun getUser(): String {
        return preferences.getString(EMAIL,"").toString()
    }

    fun setNama(value: String?) {
        editor.putString(EMAIL, value)
        editor.apply()
    }

    fun getNama(): String {
        return preferences.getString(EMAIL,"").toString()
    }

    fun setLatitude(value: String?) {
        editor.putString(LATITUDE, value)
        editor.apply()
    }

    fun getLatitude(): String {
        return preferences.getString(LATITUDE,"").toString()
    }

    fun setLongitude(value: String?) {
        editor.putString(LONGITUDE, value)
        editor.apply()
    }

    fun getLongitude(): String {
        return preferences.getString(LONGITUDE,"").toString()
    }

    fun setCountryName(value: String?) {
        editor.putString(COUNTRY_NAME, value)
        editor.apply()
    }

    fun getCountryName(): String {
        return preferences.getString(COUNTRY_NAME,"").toString()
    }

    fun setLocality(value: String?) {
        editor.putString(LOCALITY, value)
        editor.apply()
    }

    fun getLocality(): String {
        return preferences.getString(LOCALITY,"").toString()
    }

    fun setAddress(value: String?) {
        editor.putString(ADDRESS, value)
        editor.apply()
    }

    fun getAddress(): String {
        return preferences.getString(ADDRESS,"").toString()
    }

    fun setLogin(value: Boolean) {
        editor.putBoolean(IS_LOGIN, true)
        editor.apply()
    }

    fun getLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN,false)
    }

    fun removeData() {
        editor.clear()
        editor.commit()
    }
}