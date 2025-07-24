package com.generals.module.video.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.generals.module.video.model.bean.Comment
import com.generals.module.video.model.bean.CommentResponse
import com.generals.module.video.model.repository.CommentRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/16 14:47
 */

class CommentViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _hotCommentLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val hotCommentLiveData: LiveData<List<Comment>> get() = _hotCommentLiveData

    fun getHotComment(videoId: Int) {
        val disposable = CommentRepository.getHotComment(videoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val itemList = response.itemList
                val hotList: MutableList<Comment> = mutableListOf()
                for (item in itemList) {
                    if (item.type == "reply") {
                        hotList.add(item)
                    }
                    if (item.type == "leftAlignTextHeader" && item.data.text == "最新评论") {
                        break
                    }
                }
                // 特殊处理，有的视频接口无最热评论故用最新评论的第一页代替
                if (itemList.isNotEmpty() && itemList[0].data.text == "最新评论") {
                    hotList.addAll(itemList.filter { it.type == "reply" })
                }
                _hotCommentLiveData.postValue(hotList)
            }, {
                Log.d(
                    "zzx",
                    "(${Error().stackTrace[0].run { "$fileName:$lineNumber" }}) -> ${it.stackTrace}"
                )
            })
        compositeDisposable.add(disposable)
    }

    fun getNewComment(videoId: Int): Flow<PagingData<Comment>> {
        return CommentRepository.getNewComment(videoId).flow.cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}