package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.illdan.desktop.core.design_system.ACTION_DELETE
import com.illdan.desktop.core.design_system.AppTypo
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray50
import com.illdan.desktop.core.design_system.Gray70
import com.illdan.desktop.core.design_system.Gray80
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.Primary40
import com.illdan.desktop.core.design_system.WORD_BOOKMARK
import com.illdan.desktop.core.design_system.Warning40
import com.illdan.desktop.core.util.DateTimeFormatter
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.todo.Todo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_arrow_left
import illdandesktop.composeapp.generated.resources.ic_arrow_right
import illdandesktop.composeapp.generated.resources.ic_calendar
import illdandesktop.composeapp.generated.resources.ic_repeat
import illdandesktop.composeapp.generated.resources.ic_star
import illdandesktop.composeapp.generated.resources.ic_three_dot
import illdandesktop.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoItem(
    item: Todo,
    isActive: Boolean = false,
    isDeadlineDateMode: Boolean = false,
    modifier: Modifier = Modifier,
    isToday: Boolean = true,
    isMenuExpanded: Boolean = false,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> },
    onCheckedChange: (TodoStatus, Long) -> Unit = { _, _ -> },
    onBookmarkClick: (Long) -> Unit = {},
    onTodoMenuClick: (Long) -> Unit = {},
    onSwiped: (Long) -> Unit,
    onDeleted: (Long) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = item.content,
                selection = TextRange(item.content.length)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Gray95)
                .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 12.dp)
        ) {
            if (isToday) {
                CommonCheckBox(
                    isChecked = item.todoStatus == TodoStatus.COMPLETED,
                    onCheckedChange = {
                        onCheckedChange(item.todoStatus, item.todoId)
                    }
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).clip(CircleShape).clickable { onSwiped(item.todoId) }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newTextFieldValue ->
                        textFieldValue = newTextFieldValue
                    },
                    textStyle = AppTypo().mdRegular.copy(color = Gray00),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                textFieldValue = textFieldValue.copy(
                                    selection = TextRange(textFieldValue.text.length)
                                )
                            }
                        }
                        .onPreviewKeyEvent { e ->
                            val isEnter = e.key == Key.Enter || e.key == Key.NumPadEnter
                            if (isEnter && e.type == KeyEventType.KeyDown) {
                                focusManager.clearFocus(force = true)
                                onClearActiveItem()
                                if (item.content != textFieldValue.text) {
                                    onTodoItemModified(item.todoId, textFieldValue.text)
                                }
                                true
                            } else if (isEnter && e.type == KeyEventType.KeyUp) {
                                true
                            } else {
                                false
                            }
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onClearActiveItem()
                            if (item.content != textFieldValue.text) onTodoItemModified(item.todoId, textFieldValue.text)
                        }
                    ),
                    cursorBrush = SolidColor(Gray00)
                )

                if (item.isBookmark || item.imageUrl.isNotBlank() || item.deadline.isNotBlank() || item.routineDays.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TodoMetaInfoSection(item)
                }
            }

            if (isToday) {
                Image(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).clip(CircleShape).clickable { onSwiped(item.todoId) }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(Res.drawable.ic_three_dot),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray80),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onTodoMenuClick(item.todoId) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(Res.drawable.ic_star),
                contentDescription = null,
                colorFilter = ColorFilter.tint(if (item.isBookmark) Primary40 else Gray70),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onBookmarkClick(item.todoId) }
            )
        }

        if (isMenuExpanded) {
            Row(
                modifier = Modifier
                    .offset(x = (-40).dp, y = 20.dp)
                    .background(Gray90, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDeleted(item.todoId) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_trash),
                    contentDescription = null,
                    tint = Warning40,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                AppText(
                    text = ACTION_DELETE,
                    style = AppTextStyle.smMedium,
                    color = Warning40
                )
            }
        }
    }
}

@Composable
private fun TodoMetaInfoSection(
    item: Todo
) {
    val deadline = DateTimeFormatter.formatDeadline(item.deadline)
    val routineDays = item.routineDays.joinToString("")

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (deadline.isNotBlank()) { TodoMetaInfoItem(icon = painterResource(Res.drawable.ic_calendar), text = deadline) }
        if (routineDays.isNotBlank()) { TodoMetaInfoItem(icon = painterResource(Res.drawable.ic_repeat), text = routineDays) }
        if (item.isBookmark) { BookmarkCategoryInfoItem(imageUrl = item.imageUrl, categoryName = item.categoryName, isBookmark = true) }
        if (item.imageUrl.isNotBlank()) { BookmarkCategoryInfoItem(imageUrl = item.imageUrl, categoryName = item.categoryName, isBookmark = false) }
    }
}

@Composable
private fun TodoMetaInfoItem(
    icon: Painter,
    text: String
) {
    Row(
        modifier = Modifier
            .background(Gray90, RoundedCornerShape(6.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = icon, contentDescription = null, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(3.dp))
        AppText(text = text, style = AppTextStyle.xsRegular, color = Gray50)
    }
}

@Composable
private fun BookmarkCategoryInfoItem(
    imageUrl: String,
    categoryName: String,
    isBookmark: Boolean
) {
    Row(
        modifier = Modifier
            .background(Gray90, RoundedCornerShape(6.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBookmark) {
            Image(
                painter = painterResource(Res.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Primary40)
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                onError = { Logger.e("IMAGE ERROR → ${it.result.throwable}") }
            )
        }
        Spacer(Modifier.width(3.dp))
        AppText(text = if (isBookmark) WORD_BOOKMARK else categoryName, style = AppTextStyle.xsRegular, color = Gray50)
    }
}