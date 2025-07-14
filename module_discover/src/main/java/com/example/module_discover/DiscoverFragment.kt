package com.example.module_discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.adapter.CategoryAdapter
import com.example.module_discover.network.bean.CategoryItem

class DiscoverFragment : Fragment() {
    private lateinit var RecyclerView:RecyclerView
    private lateinit var CardView:CardView
    private lateinit var Button:Button
    val CategoryList= listOf(
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),
        CategoryItem(R.drawable.inform,"通知",15),


    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_discover,container,false)

        if (view != null) {
            initView(view)
        } else {
            // 可选：打印日志或处理异常情况
            Log.d("DiscoverFragment", "视图创建失败，view 为 null")
        }
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    private fun initView(view: View) {
        Button=view.findViewById(R.id.button2)
        CardView=view.findViewById(R.id.search_Container)
        RecyclerView=view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3) // 3列网格
        RecyclerView.layoutManager = gridLayoutManager
        RecyclerView.adapter = CategoryAdapter(CategoryList)

    }


}
