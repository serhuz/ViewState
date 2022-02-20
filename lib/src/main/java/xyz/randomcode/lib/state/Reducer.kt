package xyz.randomcode.lib.state

import androidx.lifecycle.ViewModel
import kotlin.reflect.KProperty

typealias Reduce<State, Effect> = (State, Effect) -> State

class Reducer<S, E>(private val state: ViewState<S>, private val reduceBlock: Reduce<S, E>) {

    operator fun <R : ViewModel> getValue(thisRef: R, property: KProperty<*>): ViewState<S> = state

    fun reduce(effect: E) {
        state.updateState {
            reduceBlock.invoke(this, effect)
        }
    }
}
