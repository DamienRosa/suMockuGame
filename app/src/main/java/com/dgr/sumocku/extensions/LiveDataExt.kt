package com.dgr.sumocku.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}
