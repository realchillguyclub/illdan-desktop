package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.AppTypo
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray60
import com.illdan.desktop.core.design_system.Gray70
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun RoundedOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gray95,
    outlineColor: Color = Gray70,
    placeholder: String = "",
    enabled: Boolean = true,
    maxLength: Int = 300,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
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
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = outlineColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = {
                    if (readOnly) {
                        onClickWhenReadOnly()
                    } else {
                        focusRequester.requestFocus()
                    }
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                it.invoke()
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (value.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        AppText(
                            text = placeholder,
                            color = Gray60,
                            style = AppTextStyle.mdRegular,
                            maxLines = maxLines
                        )
                    }
                }

                BasicTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= maxLength) onValueChange(it)
                    },
                    enabled = enabled,
                    singleLine = singleLine,
                    textStyle = AppTypo().mdRegular.copy(color = Gray00),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    cursorBrush = SolidColor(Gray00),
                    readOnly = readOnly,
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }

            trailingIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it.invoke()
            }
        }
    }
}