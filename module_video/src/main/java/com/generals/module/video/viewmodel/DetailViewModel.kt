package com.generals.module.video.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.generals.module.video.model.bean.related.VideoRelated
import com.generals.module.video.model.bean.related.VideoRelatedResponse
import com.generals.module.video.model.repository.DetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Desc : 详情页的ViewModel
 * @Author : zzx
 * @Date : 2025/7/17 14:32
 */

class DetailViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _videoRelatedLiveData: MutableLiveData<List<VideoRelated>> = MutableLiveData()
    val videoRelatedLiveData: LiveData<List<VideoRelated>> get() = _videoRelatedLiveData

    fun getVideoRelated(videoId: Int) {
        val disposable = DetailRepository.getVideoRelated(videoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val videoList = response.itemList.filter { it.type == "videoSmallCard" }
                _videoRelatedLiveData.postValue(videoList)
            }, {
                Log.d(
                    "zzx",
                    "(${Error().stackTrace[0].run { "$fileName:$lineNumber" }}) -> ${it.stackTrace}"
                )
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}