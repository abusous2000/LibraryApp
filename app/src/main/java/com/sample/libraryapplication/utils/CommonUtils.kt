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
class CommonUtils{
    companion object{
        var cnt = 0;
        val images = arrayListOf<String>("https://www.palestineremembered.com/Acre/al-Bassa/Picture907.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture909.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture913.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture917.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture176.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2983.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2985.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2987.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture7177.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture7179.jpg")

        fun getRandomImageURL(): String? {
            return images.get((cnt++) % images.size)
        }
    }
}