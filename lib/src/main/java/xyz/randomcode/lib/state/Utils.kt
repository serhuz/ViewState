package xyz.randomcode.lib.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

fun <T> ViewModel.createViewState(initial: T): ViewState<T> =
    FlowViewState<T>(initial, this.viewModelScope)

fun <T, E> ViewModel.createReducer(initial: T, reduceBlock: Reduce<T, E>): Reducer<T, E> =
    Reducer(this.createViewState(initial), reduceBlock)
