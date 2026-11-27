package io.github.drlacheheb.kufus.ui.components

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class KufusIconsTest {
    @Test
    fun allInlineIconsParseValidVectorPaths() {
        val allIcons =
            listOf(
                KufusIcons.Warning,
                KufusIcons.CheckCircle,
                KufusIcons.Close,
                KufusIcons.Refresh,
                KufusIcons.Article,
                KufusIcons.DarkMode,
                KufusIcons.Info,
                KufusIcons.LightMode,
                KufusIcons.Minimize,
                KufusIcons.ArrowDropDown,
                KufusIcons.KeyboardArrowDown,
                KufusIcons.KeyboardArrowUp,
                KufusIcons.ContentCopy,
                KufusIcons.ContentCut,
                KufusIcons.ContentPaste,
                KufusIcons.Edit,
                KufusIcons.SelectAll,
            )

        allIcons.size shouldBe 17
        allIcons.forEach { icon ->
            (icon.root.size > 0) shouldBe true
        }
    }
}
