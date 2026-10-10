package com.marsgeek.translator.translation

/**
 * Phase 3: no real offline translation engine is integrated in this
 * environment, so this engine always fails with a clear reason.
 * It never returns fixed sentences, static mappings, or dictionary output.
 */
class UnavailableTranslationEngine : TranslationEngine {
    override val name: String = "unavailable"
    override suspend fun translate(input: TranslationInput): TranslationOutput {
        throw TranslationException("当前未安装可用翻译引擎。")
    }
}

class TranslationException(message: String) : Exception(message)
