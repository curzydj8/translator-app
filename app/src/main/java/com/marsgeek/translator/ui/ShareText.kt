package com.marsgeek.translator.ui

import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask

/**
 * Phase 5: the only text that may be copied or shared.
 * A completed task exposes its displayed output text and nothing else:
 * no input text, no task id, no engine name, no timestamps.
 * Any other status yields null so the copy/share buttons stay disabled.
 */
fun shareableText(task: TranslationTask): String? =
    task.outputText?.takeIf { task.status == TranslationStatus.COMPLETED }
