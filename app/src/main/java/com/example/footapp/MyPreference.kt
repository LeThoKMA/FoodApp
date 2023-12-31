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

    fun saveUser(user: User, password: String) {
        editor = pref?.edit()
        editor?.putString("id", user.id.toString())
        editor?.putString("username", user.username)
        editor?.putString("passwd", password)
        editor?.putInt("admin", user.role ?: 1)
        editor?.putString("fullname", user.fullname)
        editor?.apply()
    }

    fun getPasswd(): String {
        return pref?.getString("passwd", "").toString()
    }

    fun getUser(): User {
        var user = User()
        if (pref?.getString("id", "")?.isNotBlank() == true) {
            user = User(
                id = pref?.getString("id", "")?.toInt(),
                username = pref?.getString("username", ""),
                role = pref?.getInt("admin", 1),
                fullname = pref?.getString("fullname", ""),
            )
        }

        return user
    }

    fun logout() {
        editor = pref?.edit()
        editor?.remove("id")
        editor?.remove("username")
        editor?.remove("passwd")
        editor?.remove("salary")
        editor?.remove("admin")
        editor?.apply()
    }
}
