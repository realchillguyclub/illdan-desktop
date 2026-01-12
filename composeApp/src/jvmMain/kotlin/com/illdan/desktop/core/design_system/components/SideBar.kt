package com.illdan.desktop.core.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.illdan.desktop.core.design_system.CHECK_VERSION
import com.illdan.desktop.core.design_system.Gray10
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray95
import com.illdan.desktop.core.design_system.WORD_LOGOUT
import com.illdan.desktop.domain.enums.AppTextStyle
import com.illdan.desktop.domain.enums.MenuType
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_all
import illdandesktop.composeapp.generated.resources.ic_memo
import illdandesktop.composeapp.generated.resources.ic_person
import org.jetbrains.compose.resources.painterResource

@Composable
fun SideBar(
    onAllTodoClick: () -> Unit,
    onMemoClick: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedMenu by remember { mutableStateOf(MenuType.ALL) }
    var showSettingsDialog by remember { mutableStateOf(false) }

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
            isSelected = selectedMenu == MenuType.ALL,
            onClick = { selectedMenu = MenuType.ALL; onAllTodoClick() }
        )
        Spacer(Modifier.height(12.dp))
        SideBarMenuItem(
            icon = painterResource(Res.drawable.ic_memo),
            isSelected = selectedMenu == MenuType.NOTE,
            onClick = {
                selectedMenu = if (selectedMenu == MenuType.NOTE) MenuType.ALL else MenuType.NOTE
                onMemoClick()
            }
        )
        Spacer(Modifier.weight(1f))
        SideBarMenuItem(
            icon = painterResource(Res.drawable.ic_person),
            isSelected = false,
            showDialog = showSettingsDialog,
            onClick = { showSettingsDialog = !showSettingsDialog },
            onDismiss = { showSettingsDialog = false },
            onLogout = onLogout
        )
    }
}

@Composable
private fun SideBarMenuItem(
    icon: Painter,
    isSelected: Boolean,
    showDialog: Boolean = false,
    onClick: () -> Unit,
    onDismiss: () -> Unit = {},
    onCheckUpdate: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Box {
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

        SettingsMenuPopup(
            show = showDialog,
            onDismiss = onDismiss,
            onCheckUpdate = onCheckUpdate,
            onLogout = onLogout
        )
    }
}

@Composable
fun SettingsMenuPopup(
    show: Boolean,
    onDismiss: () -> Unit,
    onCheckUpdate: () -> Unit,
    onLogout: () -> Unit,
) {
    if (!show) return

    PopupMenu(
        alignment = Alignment.TopStart,
        offset = IntOffset(x = 108, y = -120),
        containerColor = Gray100,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.widthIn(max = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuItem(
                text = CHECK_VERSION,
                onClick = { onCheckUpdate(); onDismiss() }
            )
            HorizontalDivider(color = Gray95, thickness = 1.dp)
            MenuItem(
                text = WORD_LOGOUT,
                onClick = { onLogout(); onDismiss() }
            )
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text,
            style = AppTextStyle.mdMedium,
            color = Gray10
        )
    }
}