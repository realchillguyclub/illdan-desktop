package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.category.GroupEmojiResponse
import com.illdan.desktop.domain.model.category.Emoji
import com.illdan.desktop.domain.model.category.GroupEmoji

/**
 * mapValues는 Kotlin Map 확장 메서드
 * · Key는 그대로 유지
 * · 값만 반환
 * · 결과는 새 Map으로 반환
 * */
object GroupEmojiResponseMapper: Mapper<GroupEmojiResponse, GroupEmoji> {
    override fun responseToModel(response: GroupEmojiResponse?): GroupEmoji {
        return response?.let {
            GroupEmoji(
                groupEmojis = it.groupEmojis.mapValues { (_, emojis) ->
                    emojis.map { emoji ->
                        Emoji(
                            emojiId = emoji.emojiId,
                            imageUrl = emoji.imageUrl
                        )
                    }
                }
            )
        } ?: GroupEmoji()
    }
}