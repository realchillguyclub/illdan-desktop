package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray40
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.MESSAGE_CATEGORY_DELETE
import com.illdan.desktop.core.design_system.TITLE_CATEGORY_DELETE
import com.illdan.desktop.core.design_system.TITLE_EMPTY_CATEGORY_ICON
import com.illdan.desktop.core.design_system.TITLE_EMPTY_CATEGORY_NAME
import com.illdan.desktop.core.design_system.TITLE_LATEST_VERSION
import com.illdan.desktop.core.design_system.TITLE_LOGOUT
import com.illdan.desktop.core.design_system.WORD_CONFIRM
import com.illdan.desktop.core.design_system.Warning40
import com.illdan.desktop.domain.enums.AlertType
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun AlertDialog(
    visible: Boolean,
    alertType: AlertType,
    positiveButtonText: String = WORD_CONFIRM,
    negativeButtonText: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val title = when(alertType) {
        AlertType.CategoryDelete -> TITLE_CATEGORY_DELETE
        AlertType.VersionCheck -> TITLE_LATEST_VERSION
        AlertType.EmptyCategoryName -> TITLE_EMPTY_CATEGORY_NAME
        AlertType.EmptyCategoryIcon -> TITLE_EMPTY_CATEGORY_ICON
        AlertType.Logout -> TITLE_LOGOUT
        else -> ""
    }
    val message = when(alertType) {
        AlertType.CategoryDelete -> MESSAGE_CATEGORY_DELETE
        else -> ""
    }

    CommonDialog(
        visible = visible,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .widthIn(max = 328.dp)
                    .background(Gray100, RoundedCornerShape(24.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogContent(title, message)

                HorizontalDivider(thickness = 1.dp, color = Gray90)

                ActionButtons(
                    positiveButtonText = positiveButtonText,
                    negativeButtonText = negativeButtonText,
                    onConfirm = onConfirm,
                    onDismiss = onDismiss
                )
            }
        }
    )
}

@Composable
private fun DialogContent(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = title,
            style = if (message.isBlank()) AppTextStyle.lgMedium else AppTextStyle.lgSemiBold,
            color = Gray00
        )

        Spacer(Modifier.height(4.dp))

        if (message.isNotBlank()) {
            AppText(
                text = message,
                style = AppTextStyle.mdRegular,
                color = Gray40
            )
        }
    }
}

@Composable
private fun ActionButtons(
    positiveButtonText: String,
    negativeButtonText: String = "",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray95, RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (negativeButtonText.isNotBlank()) {
            AppText(
                text = negativeButtonText,
                style = AppTextStyle.lgSemiBold,
                color = Warning40,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onDismiss
                    )
            )

            VerticalDivider(thickness = 1.dp, color = Gray90)
        }

        AppText(
            text = positiveButtonText,
            style = AppTextStyle.lgSemiBold,
            color = Gray40,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = onConfirm
                )
        )
    }
}