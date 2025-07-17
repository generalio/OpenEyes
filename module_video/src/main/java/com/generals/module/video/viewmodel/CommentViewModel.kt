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

    private val _hotCommentLiveData: MutableLiveData<CommentResponse> = MutableLiveData()
    val hotCommentLiveData: LiveData<CommentResponse> get() = _hotCommentLiveData

    fun getHotComment(videoId: Int) {
        val disposable = CommentRepository.getHotComment(videoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                _hotCommentLiveData.postValue(it)
            } , {
                Log.d("zzx", "(${Error().stackTrace[0].run { "$fileName:$lineNumber" }}) -> ${it.stackTrace}")
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