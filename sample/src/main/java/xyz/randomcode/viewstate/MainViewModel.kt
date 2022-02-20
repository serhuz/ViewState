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

package xyz.randomcode.viewstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.randomcode.lib.state.Reducer
import xyz.randomcode.lib.state.ViewState
import xyz.randomcode.lib.state.createReducer

data class MainState(
    val firstValue: Int = 0,
    val secondValue: Int = 1
) {

    val avg: Float
        get() = (firstValue + secondValue) / 2f
}

sealed class MainEffect {
    data class UpdateFirst(val newValue: Int) : MainEffect()
    data class UpdateSecond(val newValue: Int) : MainEffect()
    object Reset : MainEffect()
}

class MainViewModel(private val initial: MainState) : ViewModel() {

    private val reducer: Reducer<MainState, MainEffect> = createReducer(initial)
    { state, effect ->
        when (effect) {
            is MainEffect.UpdateFirst -> state.copy(firstValue = effect.newValue)
            is MainEffect.UpdateSecond -> state.copy(secondValue = effect.newValue)
            is MainEffect.Reset -> initial
        }
    }

    val state: ViewState<MainState> by reducer

    fun increaseFirst() {
        reducer.reduce(MainEffect.UpdateFirst(state.current.firstValue + 1))
    }

    fun increaseSecond() {
        reducer.reduce(MainEffect.UpdateSecond(state.current.secondValue + 1))
    }

    fun reset() {
        reducer.reduce(MainEffect.Reset)
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(MainState()) as T
            } else {
                error("Class not assignable ${modelClass.name}")
            }
        }
    }
}
