package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean,
    val hidden: Boolean = false,
    @Embedded
    val attachment: Attachment?
) {
    fun toDto() =
        Post(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            content = content,
            published = published,
            likes = likes,
            likedByMe = likedByMe,
            attachment = attachment
        )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                author = dto.author,
                authorId = dto.authorId,
                authorAvatar = dto.authorAvatar,
                content = dto.content,
                published = dto.published,
                likes = dto.likes,
                likedByMe = dto.likedByMe,
                attachment = dto.attachment
            )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

