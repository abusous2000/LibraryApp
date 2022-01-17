package com.sample.libraryapplication.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.dto.TownInfo
import com.sample.libraryapplication.dto.TownPicture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PRServicesRepo @Inject constructor() {
    @Inject
    lateinit var prService: PRServices

    fun getTownInfo(districtName: String, townName: String): MutableLiveData<TownInfo?>{
        val townInfo = MutableLiveData<TownInfo?>()
        val callTownInfo = prService.getTownInfo("Acre","al-Bassa")

        callTownInfo.enqueue(object : Callback<TownInfo> {
            override fun onResponse(call: Call<TownInfo>, response: Response<TownInfo>) {
                if ( !response.isSuccessful() ){
                    Log.d("prService","Failed to get townInfo")
                    return;
                }
                if ( !response.isSuccessful() ){
                    Log.d("prService","Failed to get townInfo")
                    return;
                }
                val townInfoTmp: TownInfo? = response.body()// as TownInfo
                if (townInfoTmp != null) {
                    Log.d("Town Fragment", "Town Arabic Name: "+ townInfoTmp.arabicName)
                    townInfo.value = townInfoTmp!!
                }
            }
            override fun onFailure(call: Call<TownInfo>, t: Throwable) {
                Log.e("getting town info failed", t.message!!)
                townInfo.value = null
            }
        })


        return townInfo
    }
    fun getTownPictures(districtName: String, townName: String): MutableLiveData<List<TownPicture>?>{
        val townPictures = MutableLiveData<List<TownPicture>?>()
        val callTownPictures = prService.getTownPictures("Acre","al-Bassa")

        callTownPictures.enqueue(object : Callback<List<TownPicture>> {
            override fun onResponse(call: Call<List<TownPicture>>, response: Response<List<TownPicture>>) {
                if ( !response.isSuccessful() ){
                    Log.d("prService","Failed to get townInfo")
                    return;
                }
                if ( !response.isSuccessful() ){
                    Log.d("prService","Failed to get townInfo")
                    return;
                }
                val townPicturesTmp: List<TownPicture>? = response.body()// as TownInfo
                if (townPicturesTmp != null) {
                    Log.d("Town Fragment", "1st Town Pic title Name: "+ townPicturesTmp.get(0).title)
                    townPictures.value = townPicturesTmp!!
                 }
            }
            override fun onFailure(call: Call<List<TownPicture>>, t: Throwable) {
                Log.e("getting town pics failed", t.message!!)
                townPictures.value = null
            }
        })

        return townPictures
    }
}