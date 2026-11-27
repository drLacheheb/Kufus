package io.github.drlacheheb.kufus.ui.flasher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.drlacheheb.kufus.disk.model.FilesystemType
import io.github.drlacheheb.kufus.disk.model.ImageFormat
import io.github.drlacheheb.kufus.disk.model.ImageInfo
import io.github.drlacheheb.kufus.disk.model.PartitionScheme
import io.github.drlacheheb.kufus.disk.model.UsbDrive
import io.github.drlacheheb.kufus.disk.model.WindowsUserExperienceOptions
import io.github.drlacheheb.kufus.ui.components.KufusSectionGap
import io.github.drlacheheb.kufus.ui.components.KufusTopBar
import io.github.drlacheheb.kufus.ui.components.LocalWindowActions
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import kotlin.math.ceil

@Immutable
data class FlasherActions(
    val onBrowseImage: () -> Unit = {},
    val onSelectImagePath: (String) -> Unit = {},
    val onClearImage: () -> Unit = {},
    val onRefreshDrives: () -> Unit = {},
    val onSelectDrive: (String) -> Unit = {},
    val onSelectBootSelectionMode: (BootSelectionMode) -> Unit = {},
    val onSelectPartitionScheme: (PartitionScheme) -> Unit = {},
    val onSelectFilesystem: (FilesystemType) -> Unit = {},
    val onUpdateVolumeLabel: (String) -> Unit = {},
    val onSelectClusterSize: (Int?) -> Unit = {},
    val onToggleQuickFormat: (Boolean) -> Unit = {},
    val onFlashClick: () -> Unit = {},
    val onUpdateWindowsUserExperience: (WindowsUserExperienceOptions) -> Unit = {},
    val onConfirmWindowsUserExperience: () -> Unit = {},
    val onVerifyToggle: (Boolean) -> Unit = {},
    val onEjectToggle: (Boolean) -> Unit = {},
    val onConfirmFlash: () -> Unit = {},
    val onFormatClick: () -> Unit = {},
    val onFormatEjectToggle: (Boolean) -> Unit = {},
    val onConfirmFormat: () -> Unit = {},
    val onDismissConfirm: () -> Unit = {},
    val onCancelFlash: () -> Unit = {},
    val onDismissResult: () -> Unit = {},
    val onToggleTheme: () -> Unit = {},
    val onCloseApp: () -> Unit = {},
)

@Composable
fun FlasherScreen(
    component: FlasherComponent,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    onToggleTheme: () -> Unit = {},
    onCloseApp: () -> Unit = {},
) {
    val uiState by component.state.subscribeAsState()
    val actions =
        remember(component, onToggleTheme, onCloseApp) {
            FlasherActions(
                onBrowseImage = component::onBrowseImage,
                onSelectImagePath = component::onSelectImagePath,
                onClearImage = component::onClearImage,
                onRefreshDrives = component::onRefreshDrives,
                onSelectDrive = component::onSelectDrive,
                onSelectBootSelectionMode = component::onSelectBootSelectionMode,
                onSelectPartitionScheme = component::onSelectPartitionScheme,
                onSelectFilesystem = component::onSelectFilesystem,
                onUpdateVolumeLabel = component::onUpdateVolumeLabel,
                onSelectClusterSize = component::onSelectClusterSize,
                onToggleQuickFormat = component::onToggleQuickFormat,
                onFlashClick = component::onRequestFlash,
                onUpdateWindowsUserExperience = component::onUpdateWindowsUserExperience,
                onConfirmWindowsUserExperience = component::onConfirmWindowsUserExperience,
                onVerifyToggle = component::onToggleVerifyAfterWrite,
                onEjectToggle = component::onToggleEjectAfterWrite,
                onConfirmFlash = component::onConfirmFlash,
                onFormatClick = component::onRequestFormat,
                onFormatEjectToggle = component::onToggleEjectAfterFormat,
                onConfirmFormat = component::onConfirmFormat,
                onDismissConfirm = component::onDismissConfirmation,
                onCancelFlash = component::onCancelFlash,
                onDismissResult = component::onDismissFlashResult,
                onToggleTheme = onToggleTheme,
                onCloseApp = onCloseApp,
            )
        }

    FlasherContent(
        uiState = uiState,
        actions = actions,
        isDarkTheme = isDarkTheme,
        modifier = modifier,
    )
}

@Composable
fun FlasherContent(
    uiState: FlasherUiState,
    actions: FlasherActions,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
) {
    val palette = KufusTheme.palette
    val density = LocalDensity.current
    val windowActions = LocalWindowActions.current
    val interactive = !uiState.isFlashing
    var isDragHover by remember { mutableStateOf(false) }
    var showAdvancedFormat by remember { mutableStateOf(false) }
    var showImageInfoDialog by remember { mutableStateOf(false) }
    var showLogDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var topBarHeightPx by remember { mutableIntStateOf(0) }
    var formContentHeightPx by remember { mutableIntStateOf(0) }
    var footerHeightPx by remember { mutableIntStateOf(0) }

    val formatFieldsEnabled = interactive && (uiState.isFormattingMode || uiState.isWindowsIsoSelected)

    val formScrollState = rememberScrollState()

    LaunchedEffect(topBarHeightPx, formContentHeightPx, footerHeightPx, density, windowActions) {
        if (topBarHeightPx > 0 && formContentHeightPx > 0 && footerHeightPx > 0) {
            val totalHeightPx = topBarHeightPx + formContentHeightPx + footerHeightPx
            val preferredHeight = ceil(totalHeightPx / density.density).dp
            windowActions.onPreferredContentHeightChange(preferredHeight)
        }
    }

    Surface(
        color = palette.background,
        contentColor = palette.onSurface,
        modifier =
            modifier
                .fillMaxSize()
                .externalFileDropTarget(
                    enabled = interactive && !uiState.isInspectingImage,
                    onDragHoverChange = { isDragHover = it },
                    onFileDrop = actions.onSelectImagePath,
                ),
        border = if (isDragHover) BorderStroke(2.dp, palette.primary) else null,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KufusTopBar(
                title = "Kufus",
                modifier = Modifier.onSizeChanged { topBarHeightPx = it.height },
            )

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(formScrollState),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .onSizeChanged { formContentHeightPx = it.height }
                            .padding(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(KufusSectionGap),
                ) {
                    RufusDrivePropertiesSection(
                        uiState = uiState,
                        interactive = interactive,
                        formatFieldsEnabled = formatFieldsEnabled,
                        onShowImageInfo = { showImageInfoDialog = true },
                        actions = actions,
                    )

                    RufusFormatOptionsFields(
                        uiState = uiState,
                        formatFieldsEnabled = formatFieldsEnabled,
                        showAdvancedFormat = showAdvancedFormat,
                        onToggleAdvancedFormat = { showAdvancedFormat = !showAdvancedFormat },
                        onUpdateVolumeLabel = actions.onUpdateVolumeLabel,
                        onSelectFilesystem = actions.onSelectFilesystem,
                        onSelectClusterSize = actions.onSelectClusterSize,
                        onToggleQuickFormat = actions.onToggleQuickFormat,
                    )
                }
            }

            RufusStatusSection(
                uiState = uiState,
                isDarkTheme = isDarkTheme,
                onShowAbout = { showAboutDialog = true },
                onShowLog = { showLogDialog = true },
                actions = actions,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onSizeChanged { footerHeightPx = it.height }
                        .drawBehind {
                            if (formScrollState.maxValue > 2 && formScrollState.canScrollForward) {
                                drawLine(
                                    color = palette.outlineVariant,
                                    start = Offset.Zero,
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 1.dp.toPx(),
                                )
                            }
                        }.padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 14.dp),
            )
        }
    }

    FlasherModalDialogs(
        uiState = uiState,
        showImageInfoDialog = showImageInfoDialog,
        showLogDialog = showLogDialog,
        showAboutDialog = showAboutDialog,
        onDismissImageInfo = { showImageInfoDialog = false },
        onDismissLog = { showLogDialog = false },
        onDismissAbout = { showAboutDialog = false },
        actions = actions,
    )
}

@Composable
private fun FlasherModalDialogs(
    uiState: FlasherUiState,
    showImageInfoDialog: Boolean,
    showLogDialog: Boolean,
    showAboutDialog: Boolean,
    onDismissImageInfo: () -> Unit,
    onDismissLog: () -> Unit,
    onDismissAbout: () -> Unit,
    actions: FlasherActions,
) {
    val selectedDrive = uiState.selectedDrive
    val selectedImage = uiState.inspectedImage

    val confirmingWindows = uiState.flashState as? FlashOperationState.ConfirmingWindows
    if (confirmingWindows != null && selectedDrive != null && selectedImage != null) {
        WindowsUserExperienceDialog(
            state = confirmingWindows,
            onUpdateOptions = actions.onUpdateWindowsUserExperience,
            onConfirm = actions.onConfirmWindowsUserExperience,
            onDismiss = actions.onDismissConfirm,
        )
    }

    val confirmingFlash = uiState.flashState as? FlashOperationState.Confirming
    if (confirmingFlash != null && selectedDrive != null && selectedImage != null) {
        val windowsOpts = confirmingFlash.windowsOptions
        if (windowsOpts != null) {
            KufusDestructiveDriveDialog(
                title = "Prepare Windows USB",
                confirmLabel = "Flash",
                drive = selectedDrive,
                summaryLine = formatWindowsConfirmDetails(selectedImage, uiState.formatOptions),
                ejectLabel = "Safe-eject after flashing",
                ejectChecked = confirmingFlash.ejectAfterWrite,
                onEjectToggle = actions.onEjectToggle,
                onConfirm = actions.onConfirmFlash,
                onDismiss = actions.onDismissConfirm,
            )
        } else {
            KufusDestructiveDriveDialog(
                title = "Erase & Flash",
                confirmLabel = "Flash",
                drive = selectedDrive,
                summaryLine = "${fileDisplayName(selectedImage.path)} • ${formatImageSubtitle(selectedImage)}",
                extraToggleLabel = "Verify after flashing (SHA-256)",
                extraToggleChecked = confirmingFlash.verifyAfterWrite,
                onExtraToggle = actions.onVerifyToggle,
                ejectLabel = "Safe-eject after flashing",
                ejectChecked = confirmingFlash.ejectAfterWrite,
                onEjectToggle = actions.onEjectToggle,
                onConfirm = actions.onConfirmFlash,
                onDismiss = actions.onDismissConfirm,
            )
        }
    }

    val confirmingFormat = uiState.flashState as? FlashOperationState.ConfirmingFormat
    if (confirmingFormat != null && selectedDrive != null) {
        ConfirmFormatDialog(
            drive = selectedDrive,
            formatOptions = uiState.formatOptions,
            ejectAfterFormat = confirmingFormat.ejectAfterFormat,
            onEjectToggle = actions.onFormatEjectToggle,
            onConfirmFormat = actions.onConfirmFormat,
            onDismissConfirm = actions.onDismissConfirm,
        )
    }

    if (showImageInfoDialog && selectedImage != null) {
        RufusImageInfoDialog(image = selectedImage, onDismiss = onDismissImageInfo)
    }
    if (showLogDialog) {
        RufusLogDialog(entries = uiState.operationLog, onDismiss = onDismissLog)
    }
    if (showAboutDialog) {
        RufusAboutDialog(onDismiss = onDismissAbout)
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FlasherContentMqtlinFormatPreview() {
    KufusTheme(darkTheme = true) {
        FlasherContent(
            uiState =
                FlasherUiState(
                    drives =
                        listOf(
                            UsbDrive(
                                id = "/dev/sdb",
                                name = "sdb",
                                model = "NO_LABEL",
                                sizeBytes = 128_000_000_000L,
                            ),
                        ),
                    selectedDriveId = "/dev/sdb",
                    bootSelectionMode = BootSelectionMode.NON_BOOTABLE,
                    platformName = "Linux",
                ),
            actions = FlasherActions(),
            isDarkTheme = true,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FlasherContentMqtlinReadyPreview() {
    KufusTheme(darkTheme = true) {
        FlasherContent(
            uiState =
                FlasherUiState(
                    drives =
                        listOf(
                            UsbDrive(
                                id = "/dev/sdb",
                                name = "sdb",
                                model = "Install Debian",
                                sizeBytes = 8_000_000_000L,
                            ),
                        ),
                    selectedDriveId = "/dev/sdb",
                    selectedImagePath = "/home/user/debian-11.4.0-amd64-netinst.iso",
                    inspectedImage =
                        ImageInfo(
                            path = "/home/user/debian-11.4.0-amd64-netinst.iso",
                            sizeBytes = 400_000_000L,
                            format = ImageFormat.HYBRID_ISO,
                        ),
                    platformName = "Linux",
                ),
            actions = FlasherActions(),
            isDarkTheme = true,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FlasherContentLightPreview() {
    KufusTheme(darkTheme = false) {
        FlasherContent(
            uiState =
                FlasherUiState(
                    drives =
                        listOf(
                            UsbDrive(
                                id = "/dev/sdb",
                                name = "sdb",
                                model = "Install Debian",
                                sizeBytes = 8_000_000_000L,
                            ),
                        ),
                    selectedDriveId = "/dev/sdb",
                    selectedImagePath = "/home/user/debian-11.4.0-amd64-netinst.iso",
                    inspectedImage =
                        ImageInfo(
                            path = "/home/user/debian-11.4.0-amd64-netinst.iso",
                            sizeBytes = 400_000_000L,
                            format = ImageFormat.HYBRID_ISO,
                        ),
                    platformName = "Linux",
                ),
            actions = FlasherActions(),
            isDarkTheme = false,
        )
    }
}
