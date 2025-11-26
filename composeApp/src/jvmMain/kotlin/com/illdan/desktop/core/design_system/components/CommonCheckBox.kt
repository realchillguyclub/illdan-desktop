package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_checked
import illdandesktop.composeapp.generated.resources.ic_unchecked
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommonCheckBox(
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .size(20.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(!isChecked)
            }
            .background(color = Color.Unspecified, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(if (isChecked) Res.drawable.ic_checked else Res.drawable.ic_unchecked),
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}