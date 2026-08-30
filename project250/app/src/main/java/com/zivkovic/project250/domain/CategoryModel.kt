package com.zivkovic.project250.domain

data class CategoryModel(
    val id: Any? = null,
    var title: String = "",
    var picUrl: String = ""
) {
    val idInt: Int
        get() = when (val i = id) {
            is Number -> i.toInt()
            is String -> i.toIntOrNull() ?: 0
            else -> 0
        }
}
