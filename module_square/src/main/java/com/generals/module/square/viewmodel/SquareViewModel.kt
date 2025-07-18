package com.generals.module.square.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.generals.module.square.model.bean.ItemDetail
import com.generals.module.square.model.bean.Square
import com.generals.module.square.model.bean.SquareResponse
import com.generals.module.square.model.net.SquareRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Desc : 广场的ViewModel
 * @Author : zzx
 * @Date : 2025/7/18 10:27
 */

class SquareViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _bannerLiveData: MutableLiveData<List<ItemDetail>> = MutableLiveData()
    val bannerLiveData: LiveData<List<ItemDetail>> get() = _bannerLiveData

    private val _squareLiveData: MutableLiveData<SquareResponse> = MutableLiveData()
    val squareLiveData: LiveData<SquareResponse> get() = _squareLiveData

    fun getSquareBannerInfo() {
        val disposable = SquareRepository.getSquareBannerInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val bannerList = response.itemList.filter { it.type == "horizontalScrollCard" && it.data.dataType == "HorizontalScrollCard" }
                _bannerLiveData.postValue(bannerList[0].data.itemList)
            }, {
                Log.d("zzx", "(${Error().stackTrace[0].run { "$fileName:$lineNumber" }}) -> $it")
            })
        compositeDisposable.add(disposable)
    }

    fun getSquareInfo(startScore: String, pageCount: Int) {
        val disposable = SquareRepository.getSquareInfo(startScore, pageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _squareLiveData.postValue(response)
            }, {
                Log.d("zzx", "(${Error().stackTrace[0].run { "$fileName:$lineNumber" }}) -> ${it.stackTraceToString()}")
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}