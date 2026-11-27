package io.github.drlacheheb.kufus.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Lightweight inline 24dp Material vector icons for the 17 glyphs used by Kufus,
 * avoiding the ~38 MB `material-icons-extended` / `material` dependency footprint.
 */
object KufusIcons {
    val Warning: ImageVector by lazy {
        vectorIcon(
            name = "Warning",
            paths = listOf("M1,21h22L12,2L1,21ZM13,18h-2v-2h2v2ZM13,14h-2v-4h2v4Z"),
        )
    }

    val CheckCircle: ImageVector by lazy {
        vectorIcon(
            name = "CheckCircle",
            paths =
                listOf(
                    "M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10s10,-4.48,10,-10S17.52,2,12,2Z" +
                        "M12,20c-4.41,0,-8,-3.59,-8,-8s3.59,-8,8,-8s8,3.59,8,8s-3.59,8,-8,8Z" +
                        "M16.59,7.58L10,14.17l-2.59,-2.58L6,13l4,4l8,-8Z",
                ),
        )
    }

    val Close: ImageVector by lazy {
        vectorIcon(
            name = "Close",
            paths =
                listOf(
                    "M19,6.41L17.59,5L12,10.59L6.41,5L5,6.41L10.59,12" +
                        "L5,17.59L6.41,19L12,13.41L17.59,19L19,17.59L13.41,12Z",
                ),
        )
    }

    val Refresh: ImageVector by lazy {
        vectorIcon(
            name = "Refresh",
            paths =
                listOf(
                    "M17.65,6.35C16.2,4.9,14.21,4,12,4c-4.42,0,-7.99,3.58,-7.99,8" +
                        "s3.57,8,7.99,8c3.73,0,6.84,-2.55,7.73,-6h-2.08" +
                        "c-0.82,2.33,-3.04,4,-5.65,4c-3.31,0,-6,-2.69,-6,-6" +
                        "s2.69,-6,6,-6c1.66,0,3.14,0.69,4.22,1.78L13,11h7V4l-2.35,2.35Z",
                ),
        )
    }

    val Article: ImageVector by lazy {
        vectorIcon(
            name = "Article",
            autoMirror = true,
            paths =
                listOf(
                    "M19,5v14H5V5H19M19,3H5C3.9,3,3,3.9,3,5v14" +
                        "c0,1.1,0.9,2,2,2h14c1.1,0,2,-0.9,2,-2V5C21,3.9,20.1,3,19,3L19,3Z",
                    "M14,17H7v-2h7V17ZM17,13H7v-2h10V13ZM17,9H7V7h10V9Z",
                ),
        )
    }

    val DarkMode: ImageVector by lazy {
        vectorIcon(
            name = "DarkMode",
            paths =
                listOf(
                    "M9.37,5.51C9.19,6.15,9.1,6.82,9.1,7.5c0,4.08,3.32,7.4,7.4,7.4" +
                        "c0.68,0,1.35,-0.09,1.99,-0.27C17.45,17.19,14.93,19,12,19" +
                        "c-3.86,0,-7,-3.14,-7,-7C5,9.07,6.81,6.55,9.37,5.51Z" +
                        "M12,3c-4.97,0,-9,4.03,-9,9s4.03,9,9,9s9,-4.03,9,-9" +
                        "c0,-0.46,-0.04,-0.92,-0.1,-1.36c-0.98,1.37,-2.58,2.26,-4.4,2.26" +
                        "c-2.98,0,-5.4,-2.42,-5.4,-5.4c0,-1.81,0.89,-3.42,2.26,-4.4" +
                        "C12.92,3.04,12.46,3,12,3L12,3Z",
                ),
        )
    }

    val Info: ImageVector by lazy {
        vectorIcon(
            name = "Info",
            paths =
                listOf(
                    "M11,7h2v2h-2ZM11,11h2v6h-2Z" +
                        "M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10s10,-4.48,10,-10S17.52,2,12,2Z" +
                        "M12,20c-4.41,0,-8,-3.59,-8,-8s3.59,-8,8,-8s8,3.59,8,8s-3.59,8,-8,8Z",
                ),
        )
    }

    val LightMode: ImageVector by lazy {
        vectorIcon(
            name = "LightMode",
            paths =
                listOf(
                    "M12,9c1.65,0,3,1.35,3,3s-1.35,3,-3,3s-3,-1.35,-3,-3S10.35,9,12,9" +
                        "M12,7c-2.76,0,-5,2.24,-5,5s2.24,5,5,5s5,-2.24,5,-5S14.76,7,12,7L12,7Z" +
                        "M2,13l2,0c0.55,0,1,-0.45,1,-1s-0.45,-1,-1,-1l-2,0" +
                        "c-0.55,0,-1,0.45,-1,1S1.45,13,2,13Z" +
                        "M20,13l2,0c0.55,0,1,-0.45,1,-1s-0.45,-1,-1,-1l-2,0" +
                        "c-0.55,0,-1,0.45,-1,1S19.45,13,20,13Z" +
                        "M11,2v2c0,0.55,0.45,1,1,1s1,-0.45,1,-1V2" +
                        "c0,-0.55,-0.45,-1,-1,-1S11,1.45,11,2Z" +
                        "M11,20v2c0,0.55,0.45,1,1,1s1,-0.45,1,-1v-2" +
                        "c0,-0.55,-0.45,-1,-1,-1C11.45,19,11,19.45,11,20Z" +
                        "M5.99,4.58c-0.39,-0.39,-1.03,-0.39,-1.41,0" +
                        "c-0.39,0.39,-0.39,1.03,0,1.41l1.06,1.06" +
                        "c0.39,0.39,1.03,0.39,1.41,0s0.39,-1.03,0,-1.41L5.99,4.58Z" +
                        "M18.36,16.95c-0.39,-0.39,-1.03,-0.39,-1.41,0" +
                        "c-0.39,0.39,-0.39,1.03,0,1.41l1.06,1.06" +
                        "c0.39,0.39,1.03,0.39,1.41,0c0.39,-0.39,0.39,-1.03,0,-1.41L18.36,16.95Z" +
                        "M19.42,5.99c0.39,-0.39,0.39,-1.03,0,-1.41" +
                        "c-0.39,-0.39,-1.03,-0.39,-1.41,0l-1.06,1.06" +
                        "c-0.39,0.39,-0.39,1.03,0,1.41s1.03,0.39,1.41,0L19.42,5.99Z" +
                        "M7.05,18.36c0.39,-0.39,0.39,-1.03,0,-1.41" +
                        "c-0.39,-0.39,-1.03,-0.39,-1.41,0l-1.06,1.06" +
                        "c-0.39,0.39,-0.39,1.03,0,1.41s1.03,0.39,1.41,0L7.05,18.36Z",
                ),
        )
    }

    val Minimize: ImageVector by lazy {
        vectorIcon(name = "Minimize", paths = listOf("M6,19h12v2H6Z"))
    }

    val ArrowDropDown: ImageVector by lazy {
        vectorIcon(name = "ArrowDropDown", paths = listOf("M7,10l5,5l5,-5H7Z"))
    }

    val KeyboardArrowDown: ImageVector by lazy {
        vectorIcon(
            name = "KeyboardArrowDown",
            paths = listOf("M7.41,8.59L12,13.17l4.59,-4.58L18,10l-6,6l-6,-6l1.41,-1.41Z"),
        )
    }

    val KeyboardArrowUp: ImageVector by lazy {
        vectorIcon(
            name = "KeyboardArrowUp",
            paths = listOf("M7.41,15.41L12,10.83l4.59,4.58L18,14l-6,-6l-6,6l1.41,1.41Z"),
        )
    }

    val ContentCopy: ImageVector by lazy {
        vectorIcon(
            name = "ContentCopy",
            paths =
                listOf(
                    "M16,1L4,1c-1.1,0,-2,0.9,-2,2v14h2L4,3h12L16,1Z" +
                        "M19,5L8,5c-1.1,0,-2,0.9,-2,2v14c0,1.1,0.9,2,2,2h11" +
                        "c1.1,0,2,-0.9,2,-2L21,7c0,-1.1,-0.9,-2,-2,-2Z" +
                        "M19,21L8,21L8,7h11v14Z",
                ),
        )
    }

    val ContentCut: ImageVector by lazy {
        vectorIcon(
            name = "ContentCut",
            paths =
                listOf(
                    "M9.64,7.64c0.23,-0.5,0.36,-1.05,0.36,-1.64c0,-2.21,-1.79,-4,-4,-4" +
                        "S2,3.79,2,6s1.79,4,4,4c0.59,0,1.14,-0.13,1.64,-0.36L10,12" +
                        "l-2.36,2.36C7.14,14.13,6.59,14,6,14c-2.21,0,-4,1.79,-4,4" +
                        "s1.79,4,4,4s4,-1.79,4,-4c0,-0.59,-0.13,-1.14,-0.36,-1.64" +
                        "L12,14l7,7h3v-1L9.64,7.64Z" +
                        "M6,8c-1.1,0,-2,-0.89,-2,-2s0.9,-2,2,-2s2,0.89,2,2s-0.9,2,-2,2Z" +
                        "M6,20c-1.1,0,-2,-0.89,-2,-2s0.9,-2,2,-2s2,0.89,2,2s-0.9,2,-2,2Z" +
                        "M12,12.5c-0.28,0,-0.5,-0.22,-0.5,-0.5s0.22,-0.5,0.5,-0.5" +
                        "s0.5,0.22,0.5,0.5s-0.22,0.5,-0.5,0.5Z" +
                        "M19,3l-6,6l2,2l7,-7L22,3Z",
                ),
        )
    }

    val ContentPaste: ImageVector by lazy {
        vectorIcon(
            name = "ContentPaste",
            paths =
                listOf(
                    "M19,2h-4.18C14.4,0.84,13.3,0,12,0c-1.3,0,-2.4,0.84,-2.82,2L5,2" +
                        "c-1.1,0,-2,0.9,-2,2v16c0,1.1,0.9,2,2,2h14c1.1,0,2,-0.9,2,-2" +
                        "L21,4c0,-1.1,-0.9,-2,-2,-2Z" +
                        "M12,2c0.55,0,1,0.45,1,1s-0.45,1,-1,1s-1,-0.45,-1,-1s0.45,-1,1,-1Z" +
                        "M19,20L5,20L5,4h2v3h10L17,4h2v16Z",
                ),
        )
    }

    val Edit: ImageVector by lazy {
        vectorIcon(
            name = "Edit",
            paths =
                listOf(
                    "M3,17.25V21h3.75L17.81,9.94l-3.75,-3.75L3,17.25Z" +
                        "M20.71,7.04c0.39,-0.39,0.39,-1.02,0,-1.41l-2.34,-2.34" +
                        "c-0.39,-0.39,-1.02,-0.39,-1.41,0l-1.83,1.83l3.75,3.75l1.83,-1.83Z",
                ),
        )
    }

    val SelectAll: ImageVector by lazy {
        vectorIcon(
            name = "SelectAll",
            paths =
                listOf(
                    "M3,5h2L5,3c-1.1,0,-2,0.9,-2,2ZM3,13h2v-2L3,11v2Z" +
                        "M7,21h2v-2L7,19v2ZM3,9h2L5,7L3,7v2ZM13,3h-2v2h2L13,3Z" +
                        "M19,3v2h2c0,-1.1,-0.9,-2,-2,-2ZM5,21v-2L3,19c0,1.1,0.9,2,2,2Z" +
                        "M3,17h2v-2L3,15v2ZM9,3L7,3v2h2L9,3ZM11,21h2v-2h-2v2Z" +
                        "M19,13h2v-2h-2v2ZM19,21c1.1,0,2,-0.9,2,-2h-2v2Z" +
                        "M19,9h2L21,7h-2v2ZM19,17h2v-2h-2v2ZM15,21h2v-2h-2v2Z" +
                        "M15,5h2L17,3h-2v2ZM7,17h10L17,7L7,7v10ZM9,9h6v6L9,15L9,9Z",
                ),
        )
    }

    private fun vectorIcon(
        name: String,
        paths: List<String>,
        autoMirror: Boolean = false,
    ): ImageVector =
        ImageVector
            .Builder(
                name = name,
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
                autoMirror = autoMirror,
            ).apply {
                for (pathString in paths) {
                    addPath(
                        pathData = PathParser().parsePathString(pathString).toNodes(),
                        fill = SolidColor(Color.Black),
                    )
                }
            }.build()
}
