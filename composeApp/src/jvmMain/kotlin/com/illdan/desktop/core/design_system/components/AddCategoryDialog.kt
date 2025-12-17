package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.illdan.desktop.core.design_system.ACTION_CLOSE
import com.illdan.desktop.core.design_system.ACTION_CREATE
import com.illdan.desktop.core.design_system.ADD_CATEGORY_DIALOG_TITLE
import com.illdan.desktop.core.design_system.EDIT_CATEGORY_DIALOG_TITLE
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray40
import com.illdan.desktop.core.design_system.Gray60
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.PLACEHOLDER_CATEGORY_TEXT_FIELD
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.Emoji
import com.illdan.desktop.domain.model.category.GroupEmoji

@Composable
fun CategoryDialog(
    visible: Boolean,
    selectedCategory: Category?,
    groupEmoji: GroupEmoji,
    isEdit: Boolean = false,
    onDismiss: () -> Unit,
    onCreateClick: (String, Long) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var emoji by remember { mutableStateOf(Emoji()) }
    var categoryName by remember { mutableStateOf("") }

    LaunchedEffect(visible, selectedCategory?.id) {
        if (visible) {
            emoji = Emoji(
                imageUrl = selectedCategory?.imageUrl.orEmpty(),
                emojiId = selectedCategory?.emojiId ?: -1L
            )
            categoryName = selectedCategory?.name.orEmpty()
        }
    }

    CommonDialog(
        visible = visible,
        onDismiss = onDismiss,
        content = {
            BoxWithConstraints(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // 현재 창 높이
                val h = maxHeight

                val verticalPadding = when {
                    h <= 450.dp -> 10.dp
                    h <= 520.dp -> 30.dp
                    else -> 60.dp
                }

                Column(
                    modifier = Modifier
                        .padding(vertical = verticalPadding)
                        .aspectRatio(568f / 475f)
                        .background(Gray95, RoundedCornerShape(24.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {}
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DialogTitle(isEdit)
                    Spacer(Modifier.height(40.dp))
                    CategoryInfoSection(
                        selectedEmojiUrl = emoji.imageUrl,
                        categoryName = categoryName,
                        onValueChange = { categoryName = it }
                    )
                    Spacer(Modifier.height(40.dp))
                    EmojiList(
                        modifier = Modifier.weight(1f),
                        groupEmoji = groupEmoji,
                        onEmojiSelected = { emoji = it }
                    )
                    Spacer(Modifier.height(36.dp))
                    ActionButtons(onCloseClick = onDismiss, onCreateClick = {
                        onCreateClick(categoryName, emoji.emojiId)
                        onDismiss()
                    })
                }
            }
        }
    )
}

@Composable
private fun DialogTitle(
    isEdit: Boolean
) {
    AppText(
        text = if (isEdit) EDIT_CATEGORY_DIALOG_TITLE else ADD_CATEGORY_DIALOG_TITLE,
        style = AppTextStyle.lgMedium,
        color = Gray00
    )
}

@Composable
private fun CategoryInfoSection(
    selectedEmojiUrl: String,
    categoryName: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedEmojiUrl.isNotBlank()) {
            AsyncImage(
                model = selectedEmojiUrl,
                contentDescription = null,
                modifier = Modifier.size(44.dp)
            )
        } else {
            Box(modifier = Modifier.size(44.dp).background(Gray90, RoundedCornerShape(12.dp)))
        }

        Spacer(Modifier.width(14.dp))

        UnderlineTextField(
            value = categoryName,
            onValueChange = onValueChange,
            placeholder = PLACEHOLDER_CATEGORY_TEXT_FIELD,
        )
    }
}

@Composable
private fun EmojiList(
    modifier: Modifier,
    groupEmoji: GroupEmoji,
    onEmojiSelected: (Emoji) -> Unit
) {
    val gridState = rememberLazyGridState()
    val groups = remember(groupEmoji) { groupEmoji.groupEmojis.entries.toList() }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            state = gridState,
            modifier = Modifier.fillMaxSize().padding(end = 14.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            groups.forEach { (groupName, emojis) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AppText(
                        text = groupName,
                        style = AppTextStyle.smMedium,
                        color = Gray60
                    )
                }

                // 그룹 이모지들
                items(
                    items = emojis,
                    key = { it.emojiId }
                ) { emoji ->
                    EmojiGridItem(
                        emoji = emoji,
                        onClick = { onEmojiSelected(emoji) }
                    )
                }

                // 그룹 사이 간격
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(15.dp))
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(gridState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun EmojiGridItem(
    emoji: Emoji,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(32.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = emoji.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ActionButtons(
    onCloseClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppButton(
            text = ACTION_CLOSE,
            textColor = Gray40,
            textStyle = AppTextStyle.lgMedium,
            buttonColor = Gray90,
            onClick = onCloseClick,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(10.dp))

        AppButton(
            text = ACTION_CREATE,
            onClick = onCreateClick,
            modifier = Modifier.weight(1f)
        )
    }
}