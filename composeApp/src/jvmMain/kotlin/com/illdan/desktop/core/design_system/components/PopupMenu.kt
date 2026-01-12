package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun PopupMenu(
    alignment: Alignment,
    offset: IntOffset,
    containerColor: Color,
    properties: PopupProperties = PopupProperties(focusable = true),
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Popup(
        alignment = alignment,
        offset = offset,
        properties = properties,
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = containerColor,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp
        ) {
            content()
        }
    }
}

@Composable
fun PopupMenuItem(
    icon: Painter,
    iconColor: Color,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(start = 12.dp, end = 16.dp)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )

        Spacer(Modifier.width(4.dp))

        AppText(text = text, style = AppTextStyle.smMedium, color = textColor)
    }
}