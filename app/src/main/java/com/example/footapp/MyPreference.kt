package com.example.footapp

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.footapp.model.User

class MyPreference {
    private var accountUtil: MyPreference? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    fun getInstance(context: Context): MyPreference? {
        if (accountUtil == null) accountUtil = MyPreference()
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context)
            accountUtil?.pref = pref
        }
        return accountUtil
    }

    fun saveUser(id: String, username: String, passwd: String, salary: String, isAdmin: Int) {
        editor = pref?.edit()
        editor?.putString("id", id)
        editor?.putString("username", username)
        editor?.putString("passwd", passwd)
        editor?.putString("salary", salary)
        editor?.putInt("admin", isAdmin)
        editor?.apply()
    }

    fun getUser(): User {
        var user = User()
        if (pref?.getString("id", "")?.isNotBlank() == true) {
            user = User(
                pref?.getString("id", "")?.toInt(),
                pref?.getString("username", ""),
                pref?.getString("passwd", ""),
                pref?.getString("salary", "")?.toInt(),
                pref?.getInt("admin", 0)
            )
        }

        return user
    }

    fun logout()
    {
        editor = pref?.edit()
        editor?.remove("id")
        editor?.remove("username")
        editor?.remove("passwd")
        editor?.remove("salary")
        editor?.remove("admin")
        editor?.apply()
    }

}