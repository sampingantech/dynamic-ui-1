package id.adeds.dynamic_ui.base

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import id.adeds.dynamic_ui.di.Injection
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApp : MultiDexApplication(){

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApp)
            androidLogger(Level.ERROR)
            modules(listOf(Injection.baseModule))
        }
    }
}