package com.training.ui.admin

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.training.R
import com.training.ui.ActivityInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin.*
import java.util.*

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() , ActivityInterface {
    private val localizationDelegate = LocalizationActivityDelegate(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        supportActionBar?.hide()
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()

        var controller = findNavController(R.id.adminNavHost)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.usersFragmentMain, R.id.stadiumsFragmentMain, R.id.profileFragment2))

        setupActionBarWithNavController(controller, appBarConfiguration)

        bottomNavigationView.setupWithNavController(controller)
    }

    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    override fun setLanguage(language: String?) {
        localizationDelegate.setLanguage(this, language!!)
    }

    override fun setLanguage(locale: Locale?) {
        localizationDelegate.setLanguage(this, locale!!)
    }

    override fun getCurrentLocale(): Locale {
        return localizationDelegate.getLanguage(this)
    }

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)

    override fun onAfterLocaleChanged() {
    }

    override fun onBeforeLocaleChanged() {
    }
}