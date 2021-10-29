package com.training.ui

import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import java.util.*

interface ActivityInterface : OnLocaleChangedListener {
    fun setLanguage(language: String?)
    fun setLanguage(locale: Locale?)
    fun getCurrentLocale(): Locale
}