package com.illdan.desktop.core.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.designsystem.Gray90
import com.illdan.desktop.core.designsystem.Gray95
import com.illdan.desktop.core.designsystem.Warning40
import com.illdan.desktop.core.designsystem.components.AppText
import com.illdan.desktop.core.ui.util.noRippleClickable
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun DialogActionButtons(
    positiveText: String,
    negativeText: String = "",
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = {},
) {
    val cornerRadius = 20.dp

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Gray95, RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (negativeText.isNotBlank()) {
            DialogActionButton(
                modifier =
                    Modifier
                        .weight(1f),
                text = negativeText,
                textColor = Warning40,
                onClick = onNegativeClick,
            )

            VerticalDivider(thickness = 1.dp, color = Gray90)
        }

        DialogActionButton(
            modifier =
                Modifier
                    .weight(1f),
            text = positiveText,
            textColor = Color.White,
            onClick = onPositiveClick,
        )
    }
}

@Composable
private fun DialogActionButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppText(
            text = text,
            color = textColor,
            style = AppTextStyle.lgSemiBold,
        )
    }
}
