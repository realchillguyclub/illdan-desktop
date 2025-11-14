package com.illdan.desktop.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illdan.desktop.core.design_system.Gray00
import com.illdan.desktop.core.design_system.Gray50
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.components.AppText
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun LoginScreen(
    viewModel: AuthViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        onLoginClick = viewModel::kakaoLogin
    )
}

@Composable
private fun LoginContent(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray90)
    ) {
        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(Gray50, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { onLoginClick() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = "로그인",
                style = AppTextStyle.mdRegular,
                color = Gray00
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}