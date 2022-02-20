/*
 * Copyright 2022 Sergei Munovarov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
