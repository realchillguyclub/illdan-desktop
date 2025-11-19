package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray40
import com.illdan.desktop.core.design_system.Gray50
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.model.category.Category
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_all
import illdandesktop.composeapp.generated.resources.ic_today
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryListItem(
    category: Category,
    isSelected: Boolean = false,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit = {}
) {
    val context = LocalPlatformContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    Row(
        modifier = Modifier
            .width(180.dp)
            .background(if (isSelected) Gray90 else Gray95, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (category.imageUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(category.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                imageLoader = imageLoader,
                modifier = Modifier.size(20.dp)
            )
        } else {
            if (category.id == -2L) Image(
                painter = painterResource(Res.drawable.ic_today),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            ) else if (category.id == -1L) Image(
                painter = painterResource(Res.drawable.ic_all),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AppText(
            text = category.name,
            style = AppTextStyle.mdMedium,
            color = if (isSelected) Gray00 else Gray40,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}