package com.example.module_discover.model.net

import com.generals.lib.net.ServiceCreator

object RetrofitClient {
    val retrofit= ServiceCreator().create<CategoryService>()
}