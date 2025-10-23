package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.ADD_MEMO
import com.illdan.desktop.core.design_system.Gray10
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.model.memo.Memo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun MemoBar(
    memoList: List<Memo>,
    isCreateOpen: Boolean,
    onSelect: (Long) -> Unit,
    onMemoSubmit: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(164.dp)
            .fillMaxHeight()
            .background(Gray100)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddMemoButton(onClick = { isExpanded = !isExpanded })

        Spacer(Modifier.height(12.dp))


    }

    LaunchedEffect(isCreateOpen) {
        if (isCreateOpen) listState.animateScrollToItem(0)
    }
}

@Composable
private fun CreateMemoExtension(
    onSubmit: (String) -> Unit,
) {

}

@Composable
private fun AddMemoButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(Gray90, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(vertical = 11.5.dp)
            .padding(start = 24.dp, end = 40.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_plus),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Gray10)
        )

        Spacer(Modifier.width(8.dp))

        AppText(text = ADD_MEMO, style = AppTextStyle.smMedium, color = Gray10)
    }
}

@Composable
private fun MemoList(
    memoList: List<Memo>,
    onSelect: (Long) -> Unit
) {

}

@Composable
private fun MemoItem(
    memo: Memo
) {

}