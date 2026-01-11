package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.illdan.desktop.core.design_system.ALL_TODO
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.WORD_NOTEPAD
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_all
import illdandesktop.composeapp.generated.resources.ic_memo
import illdandesktop.composeapp.generated.resources.ic_person
import illdandesktop.composeapp.generated.resources.ic_shrink_side_bar
import org.jetbrains.compose.resources.painterResource

@Composable
fun SideBar(
    onAllTodoClick: () -> Unit,
    onMemoClick: () -> Unit
) {
    var selectedMenu by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight()
            .background(Gray100)
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SideBarMenuItem(
            icon = painterResource(Res.drawable.ic_all),
            isSelected = selectedMenu == ALL_TODO,
            onClick = { selectedMenu = ALL_TODO; onAllTodoClick() }
        )
        Spacer(Modifier.height(12.dp))
        SideBarMenuItem(
            icon = painterResource(Res.drawable.ic_memo),
            isSelected = selectedMenu == WORD_NOTEPAD,
            onClick = {
                selectedMenu = if (selectedMenu == WORD_NOTEPAD) "" else WORD_NOTEPAD
                onMemoClick()
            }
        )
        Spacer(Modifier.weight(1f))
        SideBarMenuItem(
            icon = painterResource(Res.drawable.ic_person),
            isSelected = false,
            onClick = {}
        )
    }
}

@Composable
private fun SideBarMenuItem(
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Gray95 else Gray100, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable{ onClick() }
            .height(40.dp)
            .padding(vertical = 8.dp)
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = icon, contentDescription = null, modifier = Modifier.size(24.dp))
    }
}