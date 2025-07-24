package com.generals.module.square.ui.activity

import android.os.Bundle
import android.transition.Fade
import android.view.Window
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.generals.lib.base.BaseActivity
import com.generals.module.square.R
import com.generals.module.square.model.bean.Photo
import com.generals.module.square.ui.adapter.SquareDetailAdapter

class SquareDetailActivity : BaseActivity() {

    private val photoList: MutableList<Photo> = mutableListOf()
    private var position = 0

    private lateinit var mIvBack: ImageView
    private lateinit var vp2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.sharedElementEnterTransition = Fade()
        window.sharedElementExitTransition = Fade()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_square_detail)

        // 取出传过来的list
        val getList: ArrayList<Photo>? = intent.getParcelableArrayListExtra("photoList")
        getList?.toList()?.let { photoList.addAll(it) }
        position = intent.getIntExtra("position", 0)

        mIvBack = findViewById(R.id.iv_square_back)
        vp2 = findViewById(R.id.vp2_square_detail)

        vp2.orientation = ViewPager2.ORIENTATION_VERTICAL
        vp2.adapter = SquareDetailAdapter(photoList)
        vp2.setCurrentItem(position, false)

        mIvBack.setOnClickListener {
            finish()
        }

    }

}