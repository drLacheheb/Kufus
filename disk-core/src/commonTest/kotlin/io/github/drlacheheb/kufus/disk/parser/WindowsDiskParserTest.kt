package io.github.drlacheheb.kufus.disk.parser

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class WindowsDiskParserTest {
    @Test
    fun formatsWindowsDriveModelCleanly() {
        buildWindowsDriveModel(" Generic ", " Flash Disk ", "PhysicalDrive1") shouldBe "Generic Flash Disk"
        buildWindowsDriveModel(null, null, "PhysicalDrive1") shouldBe "PhysicalDrive1"
    }

    @Test
    fun normalizesDriveLettersAndDetectsSystemMounts() {
        normalizeWindowsDriveLetter(" f:\\ ") shouldBe "F:"
        isWindowsSystemMount("c:\\", "C:") shouldBe true
        isWindowsSystemMount("E:\\", "C:") shouldBe false
    }

    @Test
    fun acceptsUsbAndRemovableCardReadersWhileRejectingInternalBuses() {
        isEligibleWindowsStorageBus(WIN_BUS_TYPE_USB, isRemovableMedia = true) shouldBe true
        isEligibleWindowsStorageBus(WIN_BUS_TYPE_USB, isRemovableMedia = false) shouldBe true
        isEligibleWindowsStorageBus(WIN_BUS_TYPE_SD, isRemovableMedia = true) shouldBe true
        isEligibleWindowsStorageBus(WIN_BUS_TYPE_MMC, isRemovableMedia = true) shouldBe true
        isEligibleWindowsStorageBus(WIN_BUS_TYPE_MMC, isRemovableMedia = false) shouldBe false
        isEligibleWindowsStorageBus(0x11, isRemovableMedia = false) shouldBe false // NVMe
        isEligibleWindowsStorageBus(0x0B, isRemovableMedia = false) shouldBe false // SATA
    }

    @Test
    fun quotesWindowsCommandLineArgumentsSafely() {
        formatWindowsCommandArguments(emptyList()) shouldBe null
        formatWindowsCommandArguments(listOf("--flash", "C:\\My Images\\ubuntu\"24\\.iso")) shouldBe
            "--flash \"C:\\My Images\\ubuntu\\\"24\\.iso\""
        formatWindowsCommandArguments(listOf("C:\\dir with space\\", "")) shouldBe
            "\"C:\\dir with space\\\\\" \"\""
    }
}
