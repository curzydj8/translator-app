package com.marsgeek.translator.ui

/**
 * Phase 2: languages supported by the language selectors.
 * English and Simplified Chinese only (Phase 2 scope).
 */
object Languages {
    data class Language(val code: String, val displayName: String)

    val SUPPORTED = listOf(
        Language("en", "\u82f1\u8bed"),
        Language("zh-CN", "\u7b80\u4f53\u4e2d\u6587")
    )

    fun displayNames(): List<String> = SUPPORTED.map { it.displayName }

    fun codeFor(displayName: String): String? =
        SUPPORTED.find { it.displayName == displayName }?.code
}
