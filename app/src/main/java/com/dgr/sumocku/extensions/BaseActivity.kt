package com.dgr.sumocku.extensions

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

open class BaseActivity: AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
}