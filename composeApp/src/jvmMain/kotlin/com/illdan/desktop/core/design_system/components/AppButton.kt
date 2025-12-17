package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Primary40
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun AppButton(
    text: String,
    textStyle: AppTextStyle = AppTextStyle.lgSemiBold,
    textColor: Color = Gray90,
    buttonColor: Color = Primary40,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(buttonColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        AppText(text = text, color = textColor, style = textStyle)
    }
}