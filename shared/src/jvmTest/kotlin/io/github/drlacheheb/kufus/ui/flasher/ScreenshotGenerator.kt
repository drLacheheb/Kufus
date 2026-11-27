package io.github.drlacheheb.kufus.ui.flasher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.drlacheheb.kufus.disk.model.FilesystemType
import io.github.drlacheheb.kufus.disk.model.FlashPhase
import io.github.drlacheheb.kufus.disk.model.HostOsKind
import io.github.drlacheheb.kufus.disk.model.ImageFormat
import io.github.drlacheheb.kufus.disk.model.ImageInfo
import io.github.drlacheheb.kufus.disk.model.PartitionScheme
import io.github.drlacheheb.kufus.disk.model.UsbDrive
import io.github.drlacheheb.kufus.disk.model.WindowsIsoMetadata
import io.github.drlacheheb.kufus.disk.model.WindowsUserExperienceOptions
import io.github.drlacheheb.kufus.ui.components.LocalInlineDialogMode
import io.github.drlacheheb.kufus.ui.components.LocalWindowActions
import io.github.drlacheheb.kufus.ui.components.WindowActions
import io.github.drlacheheb.kufus.ui.theme.KufusTheme
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.math.ceil

private const val WINDOW_WIDTH_DP = 520
private const val FALLBACK_HEIGHT_DP = 580
private const val HIDPI_SCALE = 3f
private const val DIALOG_MIN_HEIGHT_DP = 610

private data class ScreenshotSpec(
    val fileName: String,
    val uiState: FlasherUiState,
    val isDarkTheme: Boolean,
    val minHeightDp: Int = 520,
)

fun main(args: Array<String>) {
    val outputDir = File(args.firstOrNull() ?: "docs/screenshots")
    outputDir.mkdirs()

    val specs = buildShowcaseSpecs()
    for (spec in specs) {
        val outFile = File(outputDir, spec.fileName)
        renderScreenshot(spec, outFile)
        println("Generated ${outFile.absolutePath} (${outFile.length() / 1024} KiB)")
    }
}

private fun renderScreenshot(
    spec: ScreenshotSpec,
    outputFile: File,
) {
    val measuredHeightDp = measurePreferredHeightDp(spec).coerceAtLeast(spec.minHeightDp)
    val widthPx = (WINDOW_WIDTH_DP * HIDPI_SCALE).toInt()
    val heightPx = (measuredHeightDp * HIDPI_SCALE).toInt()

    val scene =
        ImageComposeScene(
            width = widthPx,
            height = heightPx,
            density = Density(HIDPI_SCALE),
        ) {
            RenderShowcaseWindow(spec = spec, onMeasuredHeight = {})
        }
    try {
        // Two render passes ensure LaunchedEffect / layout state settles before capture
        scene.render(nanoTime = 0L)
        val image = scene.render(nanoTime = 16_000_000L)
        val encoded =
            image.encodeToData(EncodedImageFormat.PNG)
                ?: error("Failed to encode PNG for ${spec.fileName}")
        outputFile.writeBytes(encoded.bytes)
    } finally {
        scene.close()
    }
}

private fun measurePreferredHeightDp(spec: ScreenshotSpec): Int {
    var preferredDp = FALLBACK_HEIGHT_DP
    val probeScene =
        ImageComposeScene(
            width = (WINDOW_WIDTH_DP * HIDPI_SCALE).toInt(),
            height = (900 * HIDPI_SCALE).toInt(),
            density = Density(HIDPI_SCALE),
        ) {
            RenderShowcaseWindow(
                spec = spec,
                onMeasuredHeight = { heightDp -> preferredDp = ceil(heightDp).toInt() + 2 },
            )
        }
    try {
        probeScene.render(nanoTime = 0L)
        probeScene.render(nanoTime = 16_000_000L)
    } finally {
        probeScene.close()
    }
    return preferredDp
}

@Composable
private fun RenderShowcaseWindow(
    spec: ScreenshotSpec,
    onMeasuredHeight: (Float) -> Unit,
) {
    val actions =
        WindowActions(
            onPreferredContentHeightChange = { dp -> onMeasuredHeight(dp.value) },
        )
    CompositionLocalProvider(
        LocalWindowActions provides actions,
        LocalInlineDialogMode provides true,
    ) {
        KufusTheme(darkTheme = spec.isDarkTheme) {
            val palette = KufusTheme.palette
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(palette.background)
                        .border(1.dp, palette.border),
            ) {
                FlasherContent(
                    uiState = spec.uiState,
                    actions = FlasherActions(),
                    isDarkTheme = spec.isDarkTheme,
                )
            }
        }
    }
}

private val showcaseSandiskDrive =
    UsbDrive(
        id = "/dev/sdb",
        name = "sdb",
        model = "SanDisk Extreme PRO",
        sizeBytes = 64_000_000_000L,
        serial = "4C530001231029118273",
        mountPoints = listOf("/media/WIN11_X64"),
    )

private val showcaseKingstonDrive =
    UsbDrive(
        id = "/dev/sdb",
        name = "sdb",
        model = "Kingston DataTraveler",
        sizeBytes = 32_000_000_000L,
        serial = "E0D55EA57410F710",
        mountPoints = emptyList(),
    )

private const val WIN11_ISO_PATH = "/home/user/Downloads/Win11_24H2_English_x64.iso"
private const val FEDORA_ISO_PATH = "/home/user/Downloads/Fedora-Workstation-Live-42-x86_64.iso"

private val showcaseWin11Image =
    ImageInfo(
        path = WIN11_ISO_PATH,
        sizeBytes = 5_821_440_000L,
        format = ImageFormat.WINDOWS_ISO,
        windowsMetadata =
            WindowsIsoMetadata(
                volumeLabel = "WIN11_X64",
                installImagePath = "sources/install.wim",
                installImageSizeBytes = 5_102_338_048L,
                largestFileBytes = 5_102_338_048L,
                totalPayloadBytes = 5_821_440_000L,
                isSplitRequiredForFat32 = true,
                isWimSplittable = true,
            ),
    )

private val showcaseFedoraImage =
    ImageInfo(
        path = FEDORA_ISO_PATH,
        sizeBytes = 2_400_000_000L,
        format = ImageFormat.HYBRID_ISO,
    )

private fun buildShowcaseSpecs(): List<ScreenshotSpec> =
    listOf(
        windowsIsoDarkSpec(),
        flashingLightSpec(),
        windowsExperienceSpec(),
        formatModeSpec(),
    )

private fun windowsIsoDarkSpec(): ScreenshotSpec =
    ScreenshotSpec(
        fileName = "kufus-windows-iso-dark.png",
        isDarkTheme = true,
        uiState =
            FlasherUiState(
                drives = listOf(showcaseSandiskDrive),
                selectedDriveId = showcaseSandiskDrive.id,
                bootSelectionMode = BootSelectionMode.DISK_OR_ISO,
                selectedImagePath = WIN11_ISO_PATH,
                inspectedImage = showcaseWin11Image,
                formatOptions =
                    FormatOptionsState(
                        partitionScheme = PartitionScheme.GPT,
                        filesystem = FilesystemType.NTFS,
                        volumeLabel = "WIN11_X64",
                    ),
                hostOs = HostOsKind.LINUX,
            ),
    )

private fun flashingLightSpec(): ScreenshotSpec =
    ScreenshotSpec(
        fileName = "kufus-flashing-light.png",
        isDarkTheme = false,
        uiState =
            FlasherUiState(
                drives = listOf(showcaseKingstonDrive),
                selectedDriveId = showcaseKingstonDrive.id,
                bootSelectionMode = BootSelectionMode.DISK_OR_ISO,
                selectedImagePath = FEDORA_ISO_PATH,
                inspectedImage = showcaseFedoraImage,
                flashState =
                    FlashOperationState.Flashing(
                        phase = FlashPhase.WRITING,
                        bytesProcessed = 1_632_000_000L,
                        totalBytes = 2_400_000_000L,
                        speedBytesPerSec = 148_900_000L,
                    ),
                formatOptions =
                    FormatOptionsState(
                        partitionScheme = PartitionScheme.GPT,
                        filesystem = FilesystemType.FAT32,
                        volumeLabel = "FEDORA_42",
                    ),
                hostOs = HostOsKind.LINUX,
            ),
    )

private fun windowsExperienceSpec(): ScreenshotSpec =
    ScreenshotSpec(
        fileName = "kufus-windows-experience.png",
        isDarkTheme = true,
        minHeightDp = DIALOG_MIN_HEIGHT_DP,
        uiState =
            FlasherUiState(
                drives = listOf(showcaseSandiskDrive),
                selectedDriveId = showcaseSandiskDrive.id,
                bootSelectionMode = BootSelectionMode.DISK_OR_ISO,
                selectedImagePath = WIN11_ISO_PATH,
                inspectedImage = showcaseWin11Image,
                flashState =
                    FlashOperationState.ConfirmingWindows(
                        options =
                            WindowsUserExperienceOptions(
                                bypassSystemRequirements = true,
                                bypassMicrosoftAccount = true,
                                disableDataCollection = true,
                                localAccountUsername = "Alex",
                                regionalLocaleTag = "en-US",
                            ),
                        defaultUsername = "Alex",
                        defaultLocaleTag = "en-US",
                    ),
                formatOptions =
                    FormatOptionsState(
                        partitionScheme = PartitionScheme.GPT,
                        filesystem = FilesystemType.FAT32,
                        volumeLabel = "WIN11_X64",
                    ),
                hostOs = HostOsKind.LINUX,
            ),
    )

private fun formatModeSpec(): ScreenshotSpec =
    ScreenshotSpec(
        fileName = "kufus-format-mode.png",
        isDarkTheme = true,
        uiState =
            FlasherUiState(
                drives = listOf(showcaseSandiskDrive),
                selectedDriveId = showcaseSandiskDrive.id,
                bootSelectionMode = BootSelectionMode.NON_BOOTABLE,
                flashState =
                    FlashOperationState.Completed(
                        verified = false,
                        elapsedMs = 1_240L,
                        ejected = true,
                        operationKind = ActiveOperationKind.FORMAT,
                        summaryDetail = "exFAT",
                    ),
                formatOptions =
                    FormatOptionsState(
                        partitionScheme = PartitionScheme.GPT,
                        filesystem = FilesystemType.EXFAT,
                        volumeLabel = "WORK_USB",
                    ),
                hostOs = HostOsKind.LINUX,
            ),
    )
