package io.github.drlacheheb.kufus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuRepresentation
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import kotlin.math.roundToInt

object KufusContextMenuRepresentation : ContextMenuRepresentation {
    @Composable
    override fun Representation(
        state: ContextMenuState,
        items: () -> List<ContextMenuItem>,
    ) {
        val status = state.status
        if (status !is ContextMenuState.Status.Open) return

        val palette = KufusTheme.palette
        val positionProvider =
            remember(status.rect) {
                CursorPopupPositionProvider(status.rect)
            }

        Popup(
            popupPositionProvider = positionProvider,
            onDismissRequest = { state.status = ContextMenuState.Status.Closed },
            properties = PopupProperties(focusable = false),
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = palette.surfaceContainer,
                border = BorderStroke(1.dp, palette.contextMenuBorder),
                shadowElevation = 8.dp,
                modifier =
                    Modifier
                        .width(180.dp)
                        .clip(RoundedCornerShape(6.dp)),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    items().forEach { item ->
                        val (icon, shortcut) = resolveMenuIconAndShortcut(item.label)
                        ContextMenuActionRow(
                            text = item.label,
                            leadingIcon = icon,
                            shortcut = shortcut,
                            onClick = {
                                state.status = ContextMenuState.Status.Closed
                                item.onClick()
                            },
                        )
                    }
                }
            }
        }
    }
}

private class CursorPopupPositionProvider(
    private val rect: Rect,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val margin = 6
        val cursorX = anchorBounds.left + rect.left.roundToInt() + 2
        val cursorY = anchorBounds.top + rect.bottom.roundToInt() + 2
        val maxX = (windowSize.width - popupContentSize.width - margin).coerceAtLeast(margin)
        val maxY = (windowSize.height - popupContentSize.height - margin).coerceAtLeast(margin)
        val x =
            if (cursorX + popupContentSize.width <= windowSize.width - margin) {
                cursorX.coerceAtLeast(margin)
            } else {
                (cursorX - popupContentSize.width - 4).coerceIn(margin, maxX)
            }
        val y =
            if (cursorY + popupContentSize.height <= windowSize.height - margin) {
                cursorY.coerceAtLeast(margin)
            } else {
                (anchorBounds.top + rect.top.roundToInt() - popupContentSize.height - 2).coerceIn(margin, maxY)
            }
        return IntOffset(x, y)
    }
}

private fun resolveMenuIconAndShortcut(label: String): Pair<ImageVector, String?> =
    when (label.lowercase().trim()) {
        "cut" -> KufusIcons.ContentCut to "Ctrl+X"
        "copy" -> KufusIcons.ContentCopy to "Ctrl+C"
        "paste" -> KufusIcons.ContentPaste to "Ctrl+V"
        "select all" -> KufusIcons.SelectAll to "Ctrl+A"
        else -> KufusIcons.Edit to null
    }

@Composable
private fun ContextMenuActionRow(
    text: String,
    leadingIcon: ImageVector,
    shortcut: String?,
    onClick: () -> Unit,
) {
    val palette = KufusTheme.palette
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val hoveredFg = if (palette.isDark) Color.White else palette.onSurface

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(26.dp)
                .background(if (isHovered) palette.surfaceBright else Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = if (isHovered) hoveredFg else palette.outline,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isHovered) hoveredFg else palette.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (shortcut != null) {
            Text(
                text = shortcut,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = if (isHovered) palette.onSurface.copy(alpha = 0.8f) else palette.outlineVariant,
            )
        }
    }
}

@Composable
actual fun ProvidePlatformContextMenu(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContextMenuRepresentation provides KufusContextMenuRepresentation,
    ) {
        content()
    }
}
