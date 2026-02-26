package ir.unalzo.di

import android.content.Context
import androidx.room.Room
import ir.unalzo.database.AppDatabase
import ir.unalzo.navigation.navigator.BottomBarNavigator
import ir.unalzo.navigation.navigator.RootNavigator
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

object DI : KoinComponent
val appModule = module {
    single {
        RootNavigator()
    }
    single {
        BottomBarNavigator()
    }
    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single {
        get<AppDatabase>()
            .taskDao()
    }
    single<Json> {
        Json
    }
}
fun Context.setupKoin(){
    startKoin {
        androidContext(this@setupKoin)
        modules(
            appModule
        )
    }
}