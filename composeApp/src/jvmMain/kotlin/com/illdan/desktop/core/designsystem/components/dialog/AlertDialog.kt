package com.illdan.desktop.core.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.designsystem.Gray00
import com.illdan.desktop.core.designsystem.Gray100
import com.illdan.desktop.core.designsystem.Gray40
import com.illdan.desktop.core.designsystem.components.AppText
import com.illdan.desktop.core.designsystem.components.CommonDialog
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun AlertDialog(
    visible: Boolean,
    title: String,
    content: String = "",
    positiveText: String,
    negativeText: String = "",
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit,
) {
    val hasContent = content.isNotBlank()
    val cornerRadius = 20.dp

    CommonDialog(
        visible = visible,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier =
                    Modifier
                        .width(328.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(Gray100, RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(48.dp))
                    AppText(
                        text = title,
                        style = if (hasContent) AppTextStyle.lgSemiBold else AppTextStyle.lgMedium,
                        color = Gray00,
                    )
                    if (hasContent) {
                        Spacer(Modifier.height(4.dp))
                        AppText(
                            text = content,
                            style = AppTextStyle.mdRegular,
                            color = Gray40,
                        )
                    }
                    Spacer(Modifier.height(48.dp))
                }

                DialogActionButtons(
                    positiveText = positiveText,
                    negativeText = negativeText,
                    onPositiveClick = onPositiveClick,
                    onNegativeClick = onNegativeClick,
                )
            }
        },
    )
}
