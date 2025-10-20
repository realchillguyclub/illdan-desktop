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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray40
import com.illdan.desktop.core.design_system.Gray50
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun CategoryListItem(
    icon: Painter,
    title: String,
    itemCount: Int,
    isSelected: Boolean = false,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(if (isSelected) Gray90 else Gray95, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            )
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        AppText(
            text = title,
            style = AppTextStyle.mdMedium,
            color = if (isSelected) Gray00 else Gray40
        )

        if (isSelected) {
            Spacer(modifier = Modifier.width(32.dp))

            AppText(
                text = "$itemCount",
                style = AppTextStyle.mdRegular,
                color = Gray50
            )
        }
    }
}