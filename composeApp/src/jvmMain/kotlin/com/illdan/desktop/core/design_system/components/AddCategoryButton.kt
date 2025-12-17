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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.Gray20
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.NEW_CATEGORY
import com.illdan.desktop.domain.enums.AppTextStyle
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddCategoryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .width(180.dp)
            .background(Gray95, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_plus),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Gray20)
        )

        Spacer(Modifier.width(8.dp))

        AppText(
            text = NEW_CATEGORY,
            style = AppTextStyle.mdMedium,
            color = Gray20,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}