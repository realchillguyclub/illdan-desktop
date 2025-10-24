package com.illdan.desktop.core.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.ADD_MEMO
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray10
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray60
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.model.memo.Memo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_plus
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun MemoBar(
    memoList: List<Memo>,
    onMemoSubmit: (Pair<String, String>) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf(-1L) }

    Row {
        Column(
            modifier = Modifier
                .width(168.dp)
                .fillMaxHeight()
                .background(Gray95)
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AddMemoButton(onClick = { isExpanded = !isExpanded })

            Spacer(Modifier.height(12.dp))

            MemoList(
                memoList = memoList,
                selectedId = selectedId,
                onSelect = { selectedId = it }
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            MemoExtension(
                onSubmit = onMemoSubmit
            )
        }
    }
}

@Composable
private fun MemoExtension(
    memo: Memo = Memo(),
    onSubmit: (Pair<String, String>) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(340.dp)
            .fillMaxHeight()
            .background(Gray100)
    ) {

    }
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
            .clickable { onClick() }
            .padding(vertical = 12.dp)
            .padding(start = 24.dp, end = 40.dp),
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
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    var listVisible by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(memoList, key = { _, it -> it.id }) { index, item ->
            var itemVisible by rememberSaveable(item.id) { mutableStateOf(false) }

            LaunchedEffect(listVisible) {
                if (listVisible) {
                    delay(40L * index)
                    itemVisible = true
                }
            }

            AnimatedVisibility(
                visible = itemVisible,
                enter = fadeIn() + slideInHorizontally { -it }
            ) {
                MemoItem(
                    memo = item,
                    isSelected = selectedId == item.id,
                    onClick = { onSelect(item.id) }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(200)
        listVisible = true
    }
}

@Composable
private fun MemoItem(
    memo: Memo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Gray100 else Gray90)
            .clickable { onClick() }
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AppText(
            text = memo.title,
            style = AppTextStyle.smMedium,
            color = Gray00
        )
        Spacer(Modifier.height(1.dp))
        AppText(
            text = memo.content,
            style = AppTextStyle.smRegular,
            color = Gray60
        )
    }
}