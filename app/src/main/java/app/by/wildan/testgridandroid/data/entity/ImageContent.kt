package app.by.wildan.testgridandroid.data.entity

data class ImageContent(val urls: ImageUrl)

data class ImageUrl(
    val regular: String,
    val small: String
)