package com.example.footapp.presenter

import android.content.Context
import com.example.footapp.DAO.DAO
import com.example.footapp.MyPreference
import com.example.footapp.interface1.ChangePassInterface

class ChangePassPresenter(var context: Context, var callback: ChangePassInterface) {
    private var dao = DAO()
    private var myPreference = MyPreference().getInstance(context)

}
