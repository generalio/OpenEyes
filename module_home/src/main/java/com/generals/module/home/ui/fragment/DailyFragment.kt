package com.generals.module.home.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.module.home.R
import com.generals.module.home.ui.activity.HomeActivity
import com.generals.module.home.ui.adapter.DailyAdapter
import com.generals.module.home.viewmodel.DailyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DailyFragment : Fragment() {

    private val viewModel: DailyViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeActivity: HomeActivity

    private val adapter = DailyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeActivity = activity as HomeActivity
        recyclerView = view.findViewById(R.id.rv_daily)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(homeActivity)

        initEvent()

    }

    private fun initEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDaily().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

}