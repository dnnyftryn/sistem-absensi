package com.aplikasi.siabsis.pref

import android.content.Context
import com.google.firebase.auth.FirebaseUser

internal class UserPreference (context : Context) {

    companion object {
        const val EMAIL = "email"
        private const val IS_LOGIN = "is login"
    }

    private val preferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun setUser(value: FirebaseUser?) {
        editor.putString(EMAIL, value?.email)
        editor.apply()
    }

    fun getUser(): String {
        return preferences.getString(EMAIL,"").toString()
    }

    fun removeData() {
        editor.clear()
        editor.commit()
    }

    fun setLogin(value: Boolean) {
        editor.putBoolean(IS_LOGIN, true)
        editor.apply()
    }

    fun getLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN,false)
    }
}