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
import androidx.lifecycle.viewModelScope

/**
 * Creates a [ViewState] that can be used without a [Reducer].
 *
 * This enables [ViewState] updates without a need to specify an effect type.
 *
 * @param[initial] Initial state
 *
 * @return A [ViewState] instance
 */
fun <T> ViewModel.createViewState(initial: T): MutableViewState<T> =
    FlowViewState<T>(initial, this.viewModelScope)

/**
 * Creates a [Reducer] instance along with [ViewState].
 *
 * [ViewState] is *delegated* to a [Reducer] instance.
 * It can be declared as a property inside [ViewModel] like this:
 * ```
 * val reducer: Reducer<SomeState, SomeEffect> = createReducer(/*...*/)
 * val viewState: ViewState<SomeState> by reducer
 * ```
 *
 * @param[initial] Initial state
 * @param[reduceBlock] A lambda that exposes a current state with an effect that needs to be applied
 *
 * @return A [Reducer] instance
 */
fun <T, E> ViewModel.createReducer(initial: T, reduceBlock: Reduce<T, E>): Reducer<T, E> =
    Reducer(this.createViewState(initial), reduceBlock)
