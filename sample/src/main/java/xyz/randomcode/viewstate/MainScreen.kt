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

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.randomcode.viewstate.ui.theme.ViewStateTheme

@Composable
fun MainScreen() {
    val vm: MainViewModel = viewModel(factory = MainViewModel.Factory())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val first = vm.state.observe(MainState::firstValue).value
        Text(
            text = "First: $first",
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        )

        val second = vm.state.observe(MainState::secondValue).value
        Text(
            text = "Second: $second",
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        )

        val avg = vm.state.observe(MainState::avg).value
        Text(
            text = "Average: $avg",
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        )

        TextButton(
            onClick = vm::increaseFirst,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
                .padding(16.dp)
        ) { Text(text = "Increase first") }
        TextButton(
            onClick = vm::increaseSecond,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
                .padding(16.dp)
        ) { Text(text = "Increase second") }
        TextButton(
            onClick = vm::reset,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
                .padding(16.dp)
        ) { Text(text = "Reset") }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ViewStateTheme {
        MainScreen()
    }
}
