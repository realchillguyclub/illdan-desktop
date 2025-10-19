package com.illdan.desktop.core.design_system

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.pretendard_black
import illdandesktop.composeapp.generated.resources.pretendard_bold
import illdandesktop.composeapp.generated.resources.pretendard_extra_bold
import illdandesktop.composeapp.generated.resources.pretendard_extra_light
import illdandesktop.composeapp.generated.resources.pretendard_light
import illdandesktop.composeapp.generated.resources.pretendard_medium
import illdandesktop.composeapp.generated.resources.pretendard_regular
import illdandesktop.composeapp.generated.resources.pretendard_semi_bold
import illdandesktop.composeapp.generated.resources.pretendard_thin
import org.jetbrains.compose.resources.Font

@Composable
fun AppFontFamily() = FontFamily(
    Font(Res.font.pretendard_black, FontWeight.Black),
    Font(Res.font.pretendard_bold, FontWeight.Bold),
    Font(Res.font.pretendard_extra_bold, FontWeight.ExtraBold),
    Font(Res.font.pretendard_extra_light, FontWeight.ExtraLight),
    Font(Res.font.pretendard_light, FontWeight.Light),
    Font(Res.font.pretendard_medium, FontWeight.Medium),
    Font(Res.font.pretendard_regular, FontWeight.Normal),
    Font(Res.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(Res.font.pretendard_thin, FontWeight.Thin)
)

data class AppTypography(
    // XXXL
    val xxxLSemiBold: TextStyle,
    val xxxLMedium: TextStyle,
    val xxxLRegular: TextStyle,
    val xxxLBold: TextStyle,

    // XXL
    val xxLSemiBold: TextStyle,
    val xxLMedium: TextStyle,
    val xxLRegular: TextStyle,
    val xxLBold: TextStyle,

    // XL
    val xLSemiBold: TextStyle,
    val xLMedium: TextStyle,
    val xLRegular: TextStyle,
    val xLBold: TextStyle,

    // LG
    val lgSemiBold: TextStyle,
    val lgMedium: TextStyle,
    val lgRegular: TextStyle,
    val lgBold: TextStyle,

    // MD
    val mdSemiBold: TextStyle,
    val mdMedium: TextStyle,
    val mdRegular: TextStyle,
    val mdBold: TextStyle,

    // SM
    val smSemiBold: TextStyle,
    val smMedium: TextStyle,
    val smRegular: TextStyle,
    val smBold: TextStyle,

    // XS
    val xsSemiBold: TextStyle,
    val xsMedium: TextStyle,
    val xsRegular: TextStyle,
    val xsBold: TextStyle,

    // XXS
    val xxsMedium: TextStyle,
    val calSemiBold: TextStyle,
    val calMedium: TextStyle,
    val calRegular: TextStyle
)

@Composable
fun AppTypo(): AppTypography {
    val fontFamily = AppFontFamily()
    fun lh(size: Int) = (size * 1.4f).sp

    // XXXL
    val xxxLSemiBold = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(24)
    )
    val xxxLMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(24)
    )
    val xxxLRegular = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(24)
    )
    val xxxLBold = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(24)
    )

    // XXL
    val xxLSemiBold = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(22)
    )
    val xxLMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(22)
    )
    val xxLRegular = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(22)
    )
    val xxLBold = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(22)
    )

    // XL
    val xLSemiBold = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(20)
    )
    val xLMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(20)
    )
    val xLRegular = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(20)
    )
    val xLBold = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(20)
    )

    // LG
    val lgSemiBold = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(18)
    )
    val lgMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(18)
    )
    val lgRegular = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(18)
    )
    val lgBold = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(18)
    )

    // MD
    val mdSemiBold = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(16)
    )
    val mdMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(16)
    )
    val mdRegular = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(16)
    )
    val mdBold = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(16)
    )

    // SM
    val smSemiBold = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(14)
    )
    val smMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(14)
    )
    val smRegular = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(14)
    )
    val smBold = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(14)
    )

    // XS
    val xsSemiBold = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(12)
    )
    val xsMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(12)
    )
    val xsRegular = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(12)
    )
    val xsBold = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = lh(12)
    )

    // XXS / CALENDAR
    val xxsMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(10)
    )
    val calSemiBold = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = lh(10)
    )
    val calMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = lh(10)
    )
    val calRegular = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = lh(10)
    )

    return AppTypography(
        xxxLSemiBold, xxxLMedium, xxxLRegular, xxxLBold,
        xxLSemiBold, xxLMedium, xxLRegular, xxLBold,
        xLSemiBold, xLMedium, xLRegular, xLBold,
        lgSemiBold, lgMedium, lgRegular, lgBold,
        mdSemiBold, mdMedium, mdRegular, mdBold,
        smSemiBold, smMedium, smRegular, smBold,
        xsSemiBold, xsMedium, xsRegular, xsBold,
        xxsMedium, calSemiBold, calMedium, calRegular
    )
}
