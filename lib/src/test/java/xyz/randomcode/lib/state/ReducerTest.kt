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

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ReducerTest {

    private val mockState: MutableViewState<Any> = mockk {
        every { updateState(any()) } answers {
            this.callOriginal()
        }
    }

    private lateinit var reducer: Reducer<Any, String>

    @Before
    fun setUp() {
        reducer = Reducer(mockState) { any, _ -> any }
    }

    @Test
    fun callUpdateState() {
        reducer.reduce("")

        verify { mockState.updateState(any()) }
    }

    @Test
    fun callUpdateStateForReduceWithLambda() {
        reducer.reduce { _ -> "" }

        verify { mockState.updateState(any()) }
    }
}
