package org.example

data class News(
    val id: Long,
    val publication_date: Long,
    val title: String,
    val place: Place?,
    val description: String,
    val site_url: String,
    val favorites_count: Int = 0,
    val comments_count: Long = 0
    ) {
    val rating: Double
        get() = 1 / (1 + Math.exp(-(favorites_count.toDouble() / (comments_count + 1))))

    override fun toString(): String {
        return "News(id=$id, publication_date=$publication_date, title='$title', place=$place, " +
                "description='$description', site_url='$site_url', favorites_count=$favorites_count, " +
                "comments_count=$comments_count, rating=$rating)"
    }
}

