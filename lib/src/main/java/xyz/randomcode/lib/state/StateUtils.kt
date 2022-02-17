package xyz.randomcode.lib.state

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option

fun <T> ViewModel.createViewState(initial: T): ViewState<T> =
    CoroutineViewState<T>(initial, this.viewModelScope)

fun <T> State<Option<T>>.ifPresent(block: (T) -> Unit) = this.value.fold({}, block)
