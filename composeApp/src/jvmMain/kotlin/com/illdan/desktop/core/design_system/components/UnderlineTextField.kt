package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.AppTypo
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray60
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gray95,
    contentPadding: Dp = 0.dp,
    placeholder: String = "",
    enabled: Boolean = true,
    maxLength: Int = 300,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    onClickWhenReadOnly: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .background(color = backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = {
                    if (readOnly) onClickWhenReadOnly()
                    else focusRequester.requestFocus()
                }
            )
            .padding(contentPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.defaultMinSize(minHeight = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(6.dp))

                Box {
                    if (value.isEmpty()) {
                        AppText(
                            text = placeholder,
                            color = Gray60,
                            style = AppTextStyle.mdRegular,
                            maxLines = maxLines
                        )
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = { if (it.length <= maxLength) onValueChange(it) },
                        enabled = enabled,
                        singleLine = singleLine,
                        textStyle = AppTypo().lgMedium.copy(color = Gray00),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        cursorBrush = SolidColor(Gray00),
                        readOnly = readOnly,
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }

                Spacer(Modifier.height(6.dp))

                HorizontalDivider(thickness = 1.dp, color = Gray90, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}