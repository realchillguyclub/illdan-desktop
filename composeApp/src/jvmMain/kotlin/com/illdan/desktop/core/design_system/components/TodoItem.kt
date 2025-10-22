package com.illdan.desktop.core.design_system.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.AppTypo
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray80
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.todo.Todo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_three_dot
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoItem(
    item: Todo,
    isActive: Boolean = false,
    isDeadlineDateMode: Boolean = false,
    modifier: Modifier = Modifier,
    isToday: Boolean = true,
    showBottomSheet: (Todo) -> Unit = {},
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> },
    onCheckedChange: (TodoStatus, Long) -> Unit = { _, _ -> },
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
    var showAnimation by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = showAnimation, label = "")
    val elevation by transition.animateDp(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 8.dp else 0.dp
    }

    val scale by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 1.1f else 1f
    }

    val yOffset by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) -50f else 0f
    }
    val elevationPx = with(LocalDensity.current) { elevation.toPx() }

    LaunchedEffect(transition) {
        snapshotFlow { transition.currentState }
            .filter { it }
            .collect {
                showAnimation = false
            }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray95)
            .padding(vertical = 16.dp)
            .padding(start = 16.dp, end = 12.dp)
            .offset { IntOffset(0, yOffset.toInt()) }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                shadowElevation = elevationPx
            )
    ) {
        if (isToday) {
            CommonCheckBox(
                isChecked = item.todoStatus == TodoStatus.COMPLETED,
                onCheckedChange = {
                    onCheckedChange(item.todoStatus, item.todoId)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

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
//            if (isActive) {
//                LaunchedEffect(Unit) {
//                    focusRequester.requestFocus()
//                }
//
//                BasicTextField(
//                    value = textFieldValue,
//                    onValueChange = { newTextFieldValue ->
//                        textFieldValue = newTextFieldValue
//                    },
//                    textStyle = AppTypo().mdRegular.copy(color = Gray00),
//                    modifier = Modifier
//                        .focusRequester(focusRequester)
//                        .onFocusChanged { focusState ->
//                            if (focusState.isFocused) {
//                                textFieldValue = textFieldValue.copy(
//                                    selection = TextRange(textFieldValue.text.length)
//                                )
//                            }
//                        },
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            keyboardController?.hide()
//                            onClearActiveItem()
//                            if (item.content != textFieldValue.text) onTodoItemModified(item.todoId, textFieldValue.text)
//                        }
//                    ),
//                    cursorBrush = SolidColor(Gray00)
//                )
//            } else {
//                AppText(
//                    text = item.content,
//                    color = Gray00,
//                    style = AppTextStyle.mdRegular,
//                    modifier = Modifier
//                        .offset(y = 1.5.dp)
//                )
//            }

            Spacer(modifier = Modifier.height(8.dp))
//            RepeatDeadlineRow(
//                item = item,
//                isDeadlineDateMode = isDeadlineDateMode
//            )
//
//            if (item.isBookmark || item.time.isNotEmpty() || item.categoryName.isNotEmpty()) {
//                Spacer(modifier = Modifier.height(8.dp))
//                BookmarkTimeCategoryItem(item)
//            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Image(
            painter = painterResource(Res.drawable.ic_three_dot),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Gray80),
            modifier = Modifier
                .size(24.dp)
                .clickable { showBottomSheet(item) }
        )
    }
}