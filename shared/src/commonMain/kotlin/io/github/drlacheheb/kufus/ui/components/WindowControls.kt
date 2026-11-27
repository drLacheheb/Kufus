package io.github.drlacheheb.kufus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import io.github.drlacheheb.kufus.ui.theme.UiLabelBold

private val CloseHoverColor = Color(0xFFE81123)
private const val TOP_BAR_HEIGHT_DP = 42

@Immutable
data class WindowActions(
    val onMinimize: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onPreferredContentHeightChange: (Dp) -> Unit = {},
    val draggableArea: @Composable (Modifier, @Composable () -> Unit) -> Unit = { modifier, content ->
        Box(modifier) { content() }
    },
)

val LocalWindowActions = compositionLocalOf { WindowActions() }

@Composable
fun KufusTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    val windowActions = LocalWindowActions.current

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(TOP_BAR_HEIGHT_DP.dp)
                .background(palette.background),
    ) {
        windowActions.draggableArea(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = title,
                        style = UiLabelBold.copy(fontSize = 13.5.sp, letterSpacing = (-0.2).sp, color = palette.onSurface),
                    )
                }

                WindowControls(height = TOP_BAR_HEIGHT_DP)
            }
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(palette.outlineVariant),
        )
    }
}

@Composable
fun WindowControls(
    modifier: Modifier = Modifier,
    height: Int = TOP_BAR_HEIGHT_DP,
) {
    val palette = KufusTheme.palette
    val actions = LocalWindowActions.current

    Row(
        modifier = modifier.height(height.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WindowControlButton(
            onClick = actions.onMinimize,
            hoverBackground = palette.windowControlHover,
        ) { isHovered ->
            Icon(
                imageVector = KufusIcons.Minimize,
                contentDescription = "Minimize",
                tint = if (isHovered) palette.onSurface else palette.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }

        WindowControlButton(
            onClick = actions.onClose,
            hoverBackground = CloseHoverColor,
        ) { isHovered ->
            Icon(
                imageVector = KufusIcons.Close,
                contentDescription = "Close",
                tint = if (isHovered) Color.White else palette.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun WindowControlButton(
    onClick: () -> Unit,
    hoverBackground: Color,
    content: @Composable (isHovered: Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(42.dp)
                .pointerHoverIcon(PointerIcon.Hand)
                .background(if (isHovered) hoverBackground else Color.Transparent)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        content(isHovered)
    }
}
