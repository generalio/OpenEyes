import com.example.module_discover.model.bean.CategoryItem
import com.example.module_discover.model.bean.ThemeItem

data class MainItem(
    val title: String,  // 添加标题字段
    val categoryList: List<CategoryItem>? = null,  // 分类列表
    val themeList: List<ThemeItem>? = null,        // 主题列表
    val sectionType: SectionType
)

enum class SectionType {
    CATEGORY,
    THEME
}