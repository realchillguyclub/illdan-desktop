package com.illdan.desktop.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.illdan.desktop.core.designsystem.ACTION_DELETE
import com.illdan.desktop.core.designsystem.ACTION_EDIT
import com.illdan.desktop.core.designsystem.AppTheme
import com.illdan.desktop.core.designsystem.Gray00
import com.illdan.desktop.core.designsystem.Gray30
import com.illdan.desktop.core.designsystem.Gray40
import com.illdan.desktop.core.designsystem.Gray50
import com.illdan.desktop.core.designsystem.Gray80
import com.illdan.desktop.core.designsystem.Gray90
import com.illdan.desktop.core.designsystem.Gray95
import com.illdan.desktop.core.designsystem.Warning40
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.model.category.Category
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_all
import illdandesktop.composeapp.generated.resources.ic_pen
import illdandesktop.composeapp.generated.resources.ic_star
import illdandesktop.composeapp.generated.resources.ic_three_dot
import illdandesktop.composeapp.generated.resources.ic_today
import illdandesktop.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CategoryListItem(
    category: Category,
    isSelected: Boolean = false,
    isMenuExpanded: Boolean = false,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCategoryMenuClick: () -> Unit = {},
) {
    val context = LocalPlatformContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val imageLoader =
        remember(context) {
            ImageLoader
                .Builder(context)
                .components { add(SvgDecoder.Factory()) }
                .build()
        }

    Box {
        Row(
            modifier =
                Modifier
                    .width(180.dp)
                    .background(if (isSelected) Gray90 else Gray95, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onClick,
                    ).padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (category.id) {
                -2L -> {
                    Image(
                        painter = painterResource(Res.drawable.ic_today),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }

                -1L -> {
                    Image(
                        painter = painterResource(Res.drawable.ic_all),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }

                0L -> {
                    Image(
                        painter = painterResource(Res.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }

                else -> {
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(context)
                                .data(category.imageUrl)
                                .crossfade(true)
                                .build(),
                        contentDescription = null,
                        imageLoader = imageLoader,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            AppText(
                text = category.name,
                style = AppTextStyle.mdMedium,
                color = if (isSelected) Gray00 else Gray40,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )

            if (isSelected && category.id !in listOf(-2L, -1L, 0L)) {
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = painterResource(Res.drawable.ic_three_dot),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable { onCategoryMenuClick() },
                )
            }
        }

        if (isMenuExpanded && isSelected) {
            PopupMenu(
                alignment = Alignment.CenterEnd,
                offset = IntOffset(x = 120, y = 100),
                containerColor = Gray90,
                onDismiss = { onCategoryMenuClick() },
            ) {
                Column {
                    PopupMenuItem(
                        icon = painterResource(Res.drawable.ic_pen),
                        iconColor = Gray50,
                        text = ACTION_EDIT,
                        textColor = Gray30,
                        onClick = onEdit,
                    )

                    HorizontalDivider(thickness = 1.dp, color = Gray80, modifier = Modifier.width(97.dp))

                    PopupMenuItem(
                        icon = painterResource(Res.drawable.ic_trash),
                        iconColor = Warning40,
                        text = ACTION_DELETE,
                        textColor = Warning40,
                        onClick = onDelete,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        CategoryListItem(
            category =
                Category(
                    id = 0L,
                    name = "카테고리",
                ),
            isSelected = true,
            isMenuExpanded = true,
        )
    }
}
