package ir.unalzo

import android.app.Application
import ir.unalzo.di.setupKoin

class KoinApp: Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }
}