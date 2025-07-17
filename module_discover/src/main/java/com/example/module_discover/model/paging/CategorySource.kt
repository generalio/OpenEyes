import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module_discover.model.bean.CategoryDetailItem
import com.example.module_discover.model.net.CategoryService
import com.example.module_discover.model.net.RetrofitClient

class CategorySource(
    private val id: Int,
    private val categoryService: CategoryService,

) : PagingSource<String, CategoryDetailItem>() {
    val firstPageUrl:String="http://baobab.kaiyanapp.com/api/v5/index/tab/category/"+id+"?page=0&udid=435865baacfc49499632ea13c5a78f944c2f28aa"

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CategoryDetailItem> {
        return try {
            val currentUrl = params.key ?: firstPageUrl

            // 替换为真实的网络请求
            val response = categoryService.getCategoryByUrl(currentUrl+"&udid=435865baacfc49499632ea13c5a78f944c2f28aa")

            LoadResult.Page(
                data = response.itemList,  // 从API响应中获取数据
                prevKey = null,
                nextKey = response.nextPageUrl
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    // getRefreshKey 方法不用改，保持原样
    override fun getRefreshKey(state: PagingState<String, CategoryDetailItem>): String? {
        return null
    }
}