package com.illdan.desktop.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.illdan.desktop.core.design_system.LocalAppTypography
import com.illdan.desktop.domain.enums.AppTextStyle

@Composable
fun AppTextStyle.toTextStyle(): TextStyle {
    val typo = LocalAppTypography.current

    return when (this) {
        // XXXL
        AppTextStyle.xxxlSemiBold -> typo.xxxLSemiBold
        AppTextStyle.xxxlMedium -> typo.xxxLMedium
        AppTextStyle.xxxlRegular -> typo.xxxLRegular
        AppTextStyle.xxxlBold -> typo.xxxLBold

        // XXL
        AppTextStyle.xxlSemiBold -> typo.xxLSemiBold
        AppTextStyle.xxlMedium -> typo.xxLMedium
        AppTextStyle.xxlRegular -> typo.xxLRegular
        AppTextStyle.xxlBold -> typo.xxLBold

        // XL
        AppTextStyle.xlSemiBold -> typo.xLSemiBold
        AppTextStyle.xlMedium -> typo.xLMedium
        AppTextStyle.xlRegular -> typo.xLRegular
        AppTextStyle.xlBold -> typo.xLBold

        // LG
        AppTextStyle.lgSemiBold -> typo.lgSemiBold
        AppTextStyle.lgMedium -> typo.lgMedium
        AppTextStyle.lgRegular -> typo.lgRegular
        AppTextStyle.lgBold -> typo.lgBold

        // MD
        AppTextStyle.mdSemiBold -> typo.mdSemiBold
        AppTextStyle.mdMedium -> typo.mdMedium
        AppTextStyle.mdRegular -> typo.mdRegular
        AppTextStyle.mdBold -> typo.mdBold

        // SM
        AppTextStyle.smSemiBold -> typo.smSemiBold
        AppTextStyle.smMedium -> typo.smMedium
        AppTextStyle.smRegular -> typo.smRegular
        AppTextStyle.smBold -> typo.smBold

        // XS
        AppTextStyle.xsSemiBold -> typo.xsSemiBold
        AppTextStyle.xsMedium -> typo.xsMedium
        AppTextStyle.xsRegular -> typo.xsRegular
        AppTextStyle.xsBold -> typo.xsBold

        // XXS / CALENDAR
        AppTextStyle.xxsMedium -> typo.xxsMedium
        AppTextStyle.calSemiBold -> typo.calSemiBold
        AppTextStyle.calMedium -> typo.calMedium
        AppTextStyle.calRegular -> typo.calRegular
    }
}
