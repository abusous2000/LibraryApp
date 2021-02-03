package com.sample.libraryapplication.utils

import android.text.Html
import android.widget.Toast
import com.sample.libraryapplication.view.MainActivity

fun showColoredToast(info: String){
    val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
    var toast = Toast.makeText(mainActivity.baseContext,
            Html.fromHtml("<font color='red' ><b>" + info + "</b></font>", Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG)
    toast.show()

}