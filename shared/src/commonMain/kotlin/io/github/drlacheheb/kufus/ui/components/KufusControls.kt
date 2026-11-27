package io.github.drlacheheb.kufus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.drlacheheb.kufus.ui.theme.BodyCompact
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import io.github.drlacheheb.kufus.ui.theme.LabelSm
import io.github.drlacheheb.kufus.ui.theme.LabelXs
import io.github.drlacheheb.kufus.ui.theme.MonoCode
import io.github.drlacheheb.kufus.ui.theme.UiLabelBold

internal val KufusFieldHeight = 36.dp
internal val KufusControlGap = 8.dp
internal val KufusSectionGap = 14.dp
internal val KufusCheckboxIndent = 30.dp // 20.dp checkbox + 10.dp gap
internal val KufusControlShape = RoundedCornerShape(4.dp)
internal val LocalInlineDialogMode = compositionLocalOf { false }

@Composable
internal fun KufusSectionHeading(
    title: String,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = UiLabelBold.copy(fontSize = 14.sp, color = palette.onSurface),
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = palette.border,
        )
    }
}

@Composable
internal fun KufusFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    Text(
        text = text,
        style = LabelXs.copy(color = palette.onSurfaceVariant),
        modifier = modifier,
    )
}

@Composable
internal fun KufusDropdownBox(
    selectedLabel: String,
    enabled: Boolean,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    dropdownContent: @Composable () -> Unit,
) {
    val palette = KufusTheme.palette
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var anchorWidthPx by remember { mutableIntStateOf(0) }

    val bgColor = if (enabled) palette.inputBackground else palette.surfaceContainerLow
    val borderColor =
        when {
            !enabled -> palette.border.copy(alpha = 0.55f)
            expanded -> palette.primary
            isHovered -> palette.borderHover
            else -> palette.border
        }
    val textColor = if (enabled) palette.onSurface else palette.outline
    val menuColor = if (palette.isDark) palette.surfaceContainerLow else palette.surfaceContainer

    Box(modifier = modifier) {
        Surface(
            color = bgColor,
            shape = KufusControlShape,
            border = BorderStroke(1.dp, borderColor),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(KufusFieldHeight)
                    .onGloballyPositioned { coords -> anchorWidthPx = coords.size.width }
                    .hoverable(interactionSource = interactionSource, enabled = enabled)
                    .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
                    .clickable(enabled = enabled) { onExpandedChange(true) },
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = selectedLabel,
                    style = BodyCompact.copy(color = textColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = KufusIcons.ArrowDropDown,
                    contentDescription = null,
                    tint = if (enabled) palette.onSurfaceVariant else palette.outline,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        val menuWidthModifier =
            if (anchorWidthPx > 0) {
                Modifier.width(with(density) { anchorWidthPx.toDp() })
            } else {
                Modifier
            }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            shape = KufusControlShape,
            containerColor = menuColor,
            border = BorderStroke(1.dp, palette.border),
            modifier = menuWidthModifier,
        ) {
            dropdownContent()
        }
    }
}

@Composable
internal fun KufusDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val palette = KufusTheme.palette
    val textColor = if (selected) palette.primary else palette.onSurface
    val itemBg = if (selected) palette.primary.copy(alpha = 0.12f) else Color.Transparent
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style =
                    BodyCompact.copy(
                        color = textColor,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        onClick = onClick,
        colors =
            MenuDefaults.itemColors(
                textColor = textColor,
                leadingIconColor = palette.onSurfaceVariant,
                trailingIconColor = palette.onSurfaceVariant,
            ),
        modifier =
            modifier
                .background(itemBg)
                .pointerHoverIcon(PointerIcon.Hand),
    )
}

@Composable
internal fun KufusCompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val bgColor = if (enabled) palette.inputBackground else palette.surfaceContainerLow
    val borderColor =
        when {
            !enabled -> palette.border.copy(alpha = 0.55f)
            isFocused -> palette.primary
            isHovered -> palette.borderHover
            else -> palette.border
        }
    val textColor = if (enabled) palette.onSurface else palette.outline

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        textStyle = MonoCode.copy(color = textColor),
        cursorBrush = SolidColor(palette.primary),
        interactionSource = interactionSource,
        modifier =
            modifier
                .fillMaxWidth()
                .height(KufusFieldHeight)
                .hoverable(interactionSource = interactionSource, enabled = enabled)
                .pointerHoverIcon(if (enabled) PointerIcon.Text else PointerIcon.Default),
        decorationBox = { innerTextField ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(KufusFieldHeight)
                        .background(bgColor, KufusControlShape)
                        .border(1.dp, borderColor, KufusControlShape)
                        .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MonoCode.copy(color = palette.onSurfaceVariant.copy(alpha = 0.45f)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
internal fun KufusActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
    destructive: Boolean = false,
) {
    val palette = KufusTheme.palette
    if (emphasized && enabled) {
        Button(
            onClick = onClick,
            enabled = true,
            shape = KufusControlShape,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 0.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
            modifier =
                modifier
                    .height(KufusFieldHeight)
                    .pointerHoverIcon(PointerIcon.Hand),
        ) {
            Text(
                text = text,
                style = UiLabelBold.copy(fontSize = 13.sp, color = palette.onPrimary),
                maxLines = 1,
            )
        }
    } else {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val borderColor =
            when {
                !enabled -> palette.border.copy(alpha = 0.55f)
                destructive -> palette.error.copy(alpha = if (isHovered) 0.75f else 0.45f)
                isHovered -> palette.borderHover
                else -> palette.border
            }
        val containerColor =
            when {
                !enabled -> palette.surfaceContainerLow
                destructive -> palette.errorContainer.copy(alpha = if (isHovered) 0.38f else 0.25f)
                isHovered -> palette.surfaceContainer
                else -> Color.Transparent
            }
        val contentColor =
            when {
                !enabled -> palette.outline.copy(alpha = 0.55f)
                destructive -> palette.error
                else -> palette.onSurface
            }

        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = KufusControlShape,
            border = BorderStroke(1.dp, borderColor),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                    disabledContainerColor = palette.surfaceContainerLow,
                    disabledContentColor = palette.outline.copy(alpha = 0.55f),
                ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
            modifier =
                modifier
                    .height(KufusFieldHeight)
                    .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default),
        ) {
            Text(
                text = text,
                style = UiLabelBold.copy(fontSize = 13.sp, color = contentColor),
                maxLines = 1,
            )
        }
    }
}

@Composable
internal fun KufusSquareIconButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    val palette = KufusTheme.palette
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val resolvedTint = tint ?: palette.onSurfaceVariant
    val bgColor =
        when {
            !enabled -> palette.surfaceContainerLow
            isHovered -> palette.surfaceBright
            else -> palette.surfaceContainer
        }
    val borderColor =
        when {
            !enabled -> palette.border.copy(alpha = 0.55f)
            isHovered -> palette.borderHover
            else -> palette.border
        }

    KufusTooltip(tooltip = contentDescription, modifier = modifier) {
        Surface(
            color = bgColor,
            shape = KufusControlShape,
            border = BorderStroke(1.dp, borderColor),
            modifier =
                Modifier
                    .size(KufusFieldHeight)
                    .hoverable(interactionSource = interactionSource, enabled = enabled)
                    .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
                    .clickable(enabled = enabled, onClick = onClick),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (enabled) resolvedTint else palette.outline.copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
internal fun KufusCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val palette = KufusTheme.palette
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 28.dp)
                .clip(KufusControlShape)
                .background(if (enabled && isHovered) palette.surfaceContainerLow else Color.Transparent)
                .hoverable(interactionSource = interactionSource, enabled = enabled)
                .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange,
                ).padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = kufusCheckboxColors(),
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = label,
            style = BodyCompact.copy(fontSize = 12.sp, color = if (enabled) palette.onSurface else palette.outline),
        )
    }
}

@Composable
internal fun KufusCollapsibleRow(
    expanded: Boolean,
    collapsedLabel: String,
    expandedLabel: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(KufusControlShape)
                .background(if (isHovered) palette.surfaceContainerLow else Color.Transparent)
                .hoverable(interactionSource = interactionSource)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(onClick = onToggle)
                .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = if (expanded) KufusIcons.KeyboardArrowUp else KufusIcons.KeyboardArrowDown,
            contentDescription = null,
            tint = if (isHovered) palette.onSurface else palette.outline,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = if (expanded) expandedLabel else collapsedLabel,
            style = LabelSm.copy(color = if (isHovered) palette.onSurface else palette.onSurfaceVariant),
        )
    }
}

@Composable
internal fun kufusCheckboxColors() =
    with(KufusTheme.palette) {
        CheckboxDefaults.colors(
            checkedColor = primary,
            checkmarkColor = onPrimary,
            uncheckedColor = outline,
            disabledCheckedColor = outlineVariant,
            disabledUncheckedColor = border,
        )
    }

@Composable
internal fun KufusModalDialog(
    title: String,
    onDismiss: () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconTint: Color? = null,
    body: @Composable ColumnScope.() -> Unit,
) {
    val palette = KufusTheme.palette
    val resolvedIconTint = iconTint ?: palette.primary
    val inlineDialogMode = LocalInlineDialogMode.current

    val dialogCard: @Composable () -> Unit = {
        Surface(
            shape = KufusControlShape,
            color = palette.background,
            contentColor = palette.onSurface,
            border = BorderStroke(1.dp, palette.border),
            shadowElevation = 12.dp,
            modifier =
                modifier
                    .fillMaxWidth()
                    .widthIn(min = 320.dp, max = 440.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = resolvedIconTint,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    Text(
                        text = title,
                        style = UiLabelBold.copy(fontSize = 14.sp, color = palette.onSurface),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    content = body,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(KufusControlGap, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    footer()
                }
            }
        }
    }

    if (inlineDialogMode) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.60f))
                    .padding(horizontal = 24.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            dialogCard()
        }
    } else {
        Dialog(onDismissRequest = onDismiss) {
            dialogCard()
        }
    }
}
