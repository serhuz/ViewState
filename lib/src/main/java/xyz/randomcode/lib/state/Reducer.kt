package xyz.randomcode.lib.state

import kotlin.reflect.KProperty

typealias Reduce<S, E> = (S, E) -> S

class Reducer<T, E>(private val state: ViewState<T>, private val reduceBlock: Reduce<T, E>) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ViewState<T> = state

    fun reduce(effect: E) {
        state.updateState {
            reduceBlock.invoke(this, effect)
        }
    }
}
