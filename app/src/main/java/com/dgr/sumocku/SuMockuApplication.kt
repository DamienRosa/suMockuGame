package com.dgr.sumocku

import android.app.Application
import com.dgr.sumocku.viewmodel.PlayViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class SuMockuApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@SuMockuApplication))

        bind<PlayViewModel>() with provider { PlayViewModel() }
    }
}