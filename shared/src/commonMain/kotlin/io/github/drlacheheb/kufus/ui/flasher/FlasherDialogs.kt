package io.github.drlacheheb.kufus.ui.flasher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.drlacheheb.kufus.AppBuildConfig
import io.github.drlacheheb.kufus.disk.format.core.filesystemDisplayName
import io.github.drlacheheb.kufus.disk.format.core.sanitizeVolumeLabel
import io.github.drlacheheb.kufus.disk.model.ImageFormat
import io.github.drlacheheb.kufus.disk.model.ImageInfo
import io.github.drlacheheb.kufus.disk.model.UsbDrive
import io.github.drlacheheb.kufus.disk.model.formatByteSize
import io.github.drlacheheb.kufus.ui.components.KufusActionButton
import io.github.drlacheheb.kufus.ui.components.KufusCheckboxRow
import io.github.drlacheheb.kufus.ui.components.KufusControlShape
import io.github.drlacheheb.kufus.ui.components.KufusFieldHeight
import io.github.drlacheheb.kufus.ui.components.KufusIcons
import io.github.drlacheheb.kufus.ui.components.KufusModalDialog
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import io.github.drlacheheb.kufus.ui.theme.LabelSm
import io.github.drlacheheb.kufus.ui.theme.MonoCode
import io.github.drlacheheb.kufus.ui.theme.MonoDetail
import io.github.drlacheheb.kufus.ui.theme.UiLabelBold

@Composable
internal fun KufusDestructiveDriveDialog(
    title: String,
    confirmLabel: String,
    drive: UsbDrive,
    summaryLine: String,
    ejectLabel: String,
    ejectChecked: Boolean,
    onEjectToggle: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    extraToggleLabel: String? = null,
    extraToggleChecked: Boolean = false,
    onExtraToggle: ((Boolean) -> Unit)? = null,
) {
    val palette = KufusTheme.palette
    KufusModalDialog(
        title = title,
        icon = KufusIcons.Warning,
        iconTint = palette.destructiveAccent,
        onDismiss = onDismiss,
        modifier = modifier,
        footer = {
            KufusActionButton(
                text = "Cancel",
                enabled = true,
                onClick = onDismiss,
            )
            Button(
                onClick = onConfirm,
                shape = KufusControlShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = palette.error,
                        contentColor = palette.onError,
                    ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier =
                    Modifier
                        .widthIn(min = 84.dp)
                        .height(KufusFieldHeight)
                        .pointerHoverIcon(PointerIcon.Hand),
            ) {
                Text(
                    text = confirmLabel,
                    style = UiLabelBold.copy(fontSize = 13.sp, color = palette.onError),
                )
            }
        },
    ) {
        Text(
            text = "All data on this drive will be permanently erased.",
            style = LabelSm.copy(fontWeight = FontWeight.Normal, color = palette.onSurface),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "${drive.model} (${formatByteSize(drive.sizeBytes)})",
                style = UiLabelBold.copy(fontSize = 13.sp, color = palette.onSurface),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = if (summaryLine.isNotBlank()) "${drive.id} • $summaryLine" else drive.id,
                style = MonoDetail.copy(color = palette.onSurfaceVariant),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (extraToggleLabel != null && onExtraToggle != null) {
                KufusCheckboxRow(
                    label = extraToggleLabel,
                    checked = extraToggleChecked,
                    onCheckedChange = onExtraToggle,
                )
            }
            KufusCheckboxRow(
                label = ejectLabel,
                checked = ejectChecked,
                onCheckedChange = onEjectToggle,
            )
        }
    }
}

@Composable
internal fun ConfirmFormatDialog(
    drive: UsbDrive,
    formatOptions: FormatOptionsState,
    ejectAfterFormat: Boolean,
    onEjectToggle: (Boolean) -> Unit,
    onConfirmFormat: () -> Unit,
    onDismissConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cleanLabel = sanitizeVolumeLabel(formatOptions.volumeLabel, formatOptions.filesystem)
    val parts =
        buildList {
            add(formatOptions.partitionScheme.name)
            add(filesystemDisplayName(formatOptions.filesystem))
            if (cleanLabel.isNotEmpty()) add(cleanLabel)
            if (!formatOptions.quickFormat) add("Full zero wipe")
        }

    KufusDestructiveDriveDialog(
        title = "Format USB Drive",
        confirmLabel = "Format",
        drive = drive,
        summaryLine = parts.joinToString(" • "),
        ejectLabel = "Safe-eject after formatting",
        ejectChecked = ejectAfterFormat,
        onEjectToggle = onEjectToggle,
        onConfirm = onConfirmFormat,
        onDismiss = onDismissConfirm,
        modifier = modifier,
    )
}

@Composable
internal fun RufusImageInfoDialog(
    image: ImageInfo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fileName = fileDisplayName(image.path)
    val formatLabel =
        when (image.format) {
            ImageFormat.HYBRID_ISO -> "Bootable ISO"
            ImageFormat.WINDOWS_ISO -> "Windows ISO"
            ImageFormat.RAW_IMAGE -> "Raw Disk Image"
            ImageFormat.COMPRESSED_IMAGE -> "Compressed (${image.compression.name})"
            ImageFormat.UNKNOWN -> "Unrecognized Image"
        }
    val sizeText =
        image.uncompressedSizeHintBytes?.let { hint ->
            "${formatByteSize(image.sizeBytes)} (~${formatByteSize(hint)} uncompressed)"
        } ?: formatByteSize(image.sizeBytes)

    KufusModalDialog(
        title = "Image Details",
        onDismiss = onDismiss,
        modifier = modifier,
        footer = {
            KufusActionButton(text = "OK", enabled = true, emphasized = true, onClick = onDismiss)
        },
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                DialogKeyValueRow(label = "File", value = fileName)
                DialogKeyValueRow(label = "Format", value = formatLabel)
                DialogKeyValueRow(label = "Size", value = sizeText)
                image.windowsMetadata?.let { meta ->
                    if (meta.volumeLabel.isNotBlank()) {
                        DialogKeyValueRow(label = "Label", value = meta.volumeLabel)
                    }
                    if (meta.installImagePath.isNotBlank()) {
                        DialogKeyValueRow(
                            label = "Install",
                            value = "${meta.installImagePath} (${formatByteSize(meta.installImageSizeBytes)})",
                        )
                    }
                }
                DialogKeyValueRow(label = "Path", value = image.path, mono = true)
            }
        }
    }
}

@Composable
private fun DialogKeyValueRow(
    label: String,
    value: String,
    mono: Boolean = false,
) {
    val palette = KufusTheme.palette
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = LabelSm.copy(fontWeight = FontWeight.Medium, color = palette.onSurfaceVariant),
            modifier = Modifier.width(56.dp),
        )
        Text(
            text = value,
            style =
                if (mono) {
                    MonoDetail.copy(color = palette.onSurfaceVariant)
                } else {
                    LabelSm.copy(fontWeight = FontWeight.Normal, color = palette.onSurface)
                },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
internal fun RufusLogDialog(
    entries: List<String>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    KufusModalDialog(
        title = "Operation Log",
        onDismiss = onDismiss,
        modifier = modifier,
        footer = {
            KufusActionButton(text = "Close", enabled = true, onClick = onDismiss)
        },
    ) {
        Surface(
            color = palette.inputBackground,
            shape = KufusControlShape,
            border = BorderStroke(1.dp, palette.border),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = 280.dp),
        ) {
            val logText =
                if (entries.isEmpty()) {
                    "No operations recorded in this session yet."
                } else {
                    entries.joinToString("\n")
                }
            Box(
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp),
            ) {
                SelectionContainer {
                    Text(
                        text = logText,
                        style = MonoCode.copy(fontSize = 11.sp, lineHeight = 16.sp, color = palette.onSurface),
                    )
                }
            }
        }
    }
}

@Composable
internal fun RufusAboutDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = KufusTheme.palette
    KufusModalDialog(
        title = "About Kufus",
        onDismiss = onDismiss,
        modifier = modifier,
        footer = {
            KufusActionButton(text = "OK", enabled = true, emphasized = true, onClick = onDismiss)
        },
    ) {
        Text(
            text = "Kufus v${AppBuildConfig.VERSION}",
            style = UiLabelBold.copy(fontSize = 14.sp, color = palette.onSurface),
        )
        Text(
            text = "USB image flasher, Windows ISO preparer, and drive formatter.",
            style = LabelSm.copy(fontWeight = FontWeight.Normal, lineHeight = 18.sp, color = palette.onSurfaceVariant),
        )
    }
}
