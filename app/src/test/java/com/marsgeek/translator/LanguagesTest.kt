package com.marsgeek.translator

import com.marsgeek.translator.ui.Languages
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Phase 2: verifies the language selector data (no business logic yet). */
class LanguagesTest {
    @Test
    fun supportedLanguagesContainEnglishAndChinese() {
        assertEquals(listOf("en", "zh-CN"), Languages.SUPPORTED.map { it.code })
    }

    @Test
    fun codeForResolvesDisplayNames() {
        assertEquals("en", Languages.codeFor("\u82f1\u8bed"))
        assertEquals("zh-CN", Languages.codeFor("\u7b80\u4f53\u4e2d\u6587"))
        assertNull(Languages.codeFor("\u6cd5\u8bed"))
    }
}
