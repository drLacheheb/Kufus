package io.github.drlacheheb.kufus.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val CanvasBackground = Color(0xFF0A0A0A)
val ModalSurface = Color(0xFF0A0A0A)
val SidebarBackground = Color(0xFF171717)
val InputBackground = Color(0xFF121212)
val FooterBackground = Color(0xFF0A0A0A)
val DarkBorder = Color(0xFF333333)
val DarkBorderHover = Color(0xFF525252)

val DarkBackground = Color(0xFF0A0A0A)
val DarkSurface = Color(0xFF0A0A0A)
val DarkSurfaceDim = Color(0xFF0A0A0A)
val DarkSurfaceContainerLowest = Color(0xFF121212)
val DarkSurfaceContainerLow = Color(0xFF141414)
val DarkSurfaceContainer = Color(0xFF171717)
val DarkSurfaceContainerHigh = Color(0xFF262626)
val DarkSurfaceContainerHighest = Color(0xFF333333)
val DarkSurfaceVariant = Color(0xFF262626)
val DarkSurfaceBright = Color(0xFF262626)

val DarkOutline = Color(0xFFA3A3A3)
val DarkOutlineMuted = Color(0xFFA3A3A3)
val DarkOutlineVariant = Color(0xFF262626)
val DarkOutlineHover = Color(0xFF525252)

val KufusPrimary = Color(0xFFFAFAFA)
val KufusOnPrimary = Color(0xFF0A0A0A)
val KufusAccentText = Color(0xFFFAFAFA)
val KufusPrimaryContainer = Color(0xFF262626)
val KufusOnPrimaryContainer = Color(0xFFFAFAFA)
val KufusPrimaryFixed = Color(0xFFE5E5E5)
val KufusPrimaryFixedDim = Color(0xFFD4D4D4)
val KufusInversePrimary = Color(0xFF171717)

val KufusSecondary = Color(0xFF22C55E)
val KufusOnSecondary = Color(0xFF052E16)
val KufusSecondaryContainer = Color(0xFF15803D)

val KufusTertiary = Color(0xFFF59E0B)
val KufusOnTertiary = Color(0xFF451A03)
val KufusTertiaryContainer = Color(0xFFB45309)

val KufusError = Color(0xFFDC2626)
val KufusOnError = Color(0xFFFAFAFA)
val KufusErrorContainer = Color(0xFF450A0A)
val KufusOnErrorContainer = Color(0xFFFCA5A5)
val KufusDestructiveAccent = Color(0xFFEF4444)

val DarkOnSurface = Color(0xFFFAFAFA)
val DarkOnSurfaceVariant = Color(0xFFD4D4D4)
val DarkInverseSurface = Color(0xFFFAFAFA)

val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceDim = Color(0xFFF5F5F5)
val LightSurfaceContainerLowest = Color(0xFFF5F5F5)
val LightSurfaceContainerLow = Color(0xFFF5F5F5)
val LightSurfaceContainer = Color(0xFFFFFFFF)
val LightSurfaceContainerHigh = Color(0xFFF5F5F5)
val LightSurfaceContainerHighest = Color(0xFFE5E5E5)
val LightSurfaceVariant = Color(0xFFF5F5F5)
val LightSurfaceBright = Color(0xFFF5F5F5)
val LightInputBackground = Color(0xFFFFFFFF)
val LightBorder = Color(0xFFD4D4D4)
val LightBorderHover = Color(0xFFA3A3A3)
val LightOutline = Color(0xFF666666)
val LightOutlineVariant = Color(0xFFE5E5E5)
val LightOnSurface = Color(0xFF0A0A0A)
val LightOnSurfaceVariant = Color(0xFF404040)
val LightPrimary = Color(0xFF171717)
val LightOnPrimary = Color(0xFFFAFAFA)
val LightAccentText = Color(0xFF171717)
val LightPrimaryContainer = Color(0xFFF5F5F5)
val LightOnPrimaryContainer = Color(0xFF171717)
val LightSecondary = Color(0xFF15803D)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFF15803D)
val LightTertiary = Color(0xFFB45309)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightError = Color(0xFFDC2626)
val LightOnError = Color(0xFFFAFAFA)
val LightErrorContainer = Color(0xFFFEF2F2)
val LightOnErrorContainer = Color(0xFF991B1B)
val LightDestructiveAccent = Color(0xFFDC2626)

@Immutable
data class KufusPalette(
    val isDark: Boolean,
    val background: Color,
    val surfaceDim: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceBright: Color,
    val inputBackground: Color,
    val border: Color,
    val borderHover: Color,
    val outline: Color,
    val outlineVariant: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val primary: Color,
    val onPrimary: Color,
    val accentText: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val destructiveAccent: Color,
    val contextMenuBorder: Color,
    val windowControlHover: Color,
)

val DarkKufusPalette =
    KufusPalette(
        isDark = true,
        background = DarkBackground,
        surfaceDim = DarkSurfaceDim,
        surfaceContainerLowest = DarkSurfaceContainerLowest,
        surfaceContainerLow = DarkSurfaceContainerLow,
        surfaceContainer = DarkSurfaceContainer,
        surfaceBright = DarkSurfaceBright,
        inputBackground = InputBackground,
        border = DarkBorder,
        borderHover = DarkBorderHover,
        outline = DarkOutline,
        outlineVariant = DarkOutlineVariant,
        onSurface = DarkOnSurface,
        onSurfaceVariant = DarkOnSurfaceVariant,
        primary = KufusPrimary,
        onPrimary = KufusOnPrimary,
        accentText = KufusAccentText,
        secondary = KufusSecondary,
        secondaryContainer = KufusSecondaryContainer,
        onSecondaryContainer = Color.White,
        tertiary = KufusTertiary,
        error = KufusError,
        onError = KufusOnError,
        errorContainer = KufusErrorContainer,
        onErrorContainer = KufusOnErrorContainer,
        destructiveAccent = KufusDestructiveAccent,
        contextMenuBorder = DarkOutlineVariant,
        windowControlHover = DarkSurfaceVariant,
    )

val LightKufusPalette =
    KufusPalette(
        isDark = false,
        background = LightBackground,
        surfaceDim = LightSurfaceDim,
        surfaceContainerLowest = LightSurfaceContainerLowest,
        surfaceContainerLow = LightSurfaceContainerLow,
        surfaceContainer = LightSurfaceContainer,
        surfaceBright = LightSurfaceContainerHigh,
        inputBackground = LightInputBackground,
        border = LightBorder,
        borderHover = LightBorderHover,
        outline = LightOutline,
        outlineVariant = LightOutlineVariant,
        onSurface = LightOnSurface,
        onSurfaceVariant = LightOnSurfaceVariant,
        primary = LightPrimary,
        onPrimary = LightOnPrimary,
        accentText = LightAccentText,
        secondary = LightSecondary,
        secondaryContainer = LightSecondaryContainer,
        onSecondaryContainer = Color.White,
        tertiary = LightTertiary,
        error = LightError,
        onError = LightOnError,
        errorContainer = LightErrorContainer,
        onErrorContainer = LightOnErrorContainer,
        destructiveAccent = LightDestructiveAccent,
        contextMenuBorder = LightBorder,
        windowControlHover = LightSurfaceContainerHigh,
    )

fun kufusPalette(isDark: Boolean): KufusPalette = if (isDark) DarkKufusPalette else LightKufusPalette
