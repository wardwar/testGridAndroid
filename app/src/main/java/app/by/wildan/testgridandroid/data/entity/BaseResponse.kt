package app.by.wildan.testgridandroid.data.entity

data class BaseResponse(
    val total: Int?,
    val total_pages: Int?,
    val results: List<ImageContent>?

)