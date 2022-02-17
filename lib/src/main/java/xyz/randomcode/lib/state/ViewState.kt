package xyz.randomcode.lib.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import arrow.optics.Getter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

typealias Update<T> = T.() -> T

abstract class ViewState<T>(val initial: T) {

    abstract fun updateState(block: Update<T>)

    @Composable
    abstract fun observeStateUpdates(): State<T>

    @Composable
    abstract fun <P> observeProperty(getter: Getter<T, P>): State<Option<P>>
}

class CoroutineViewState<T>(initial: T, private val scope: CoroutineScope) : ViewState<T>(initial) {

    private val singleThreadCxt: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val stateFlow: MutableStateFlow<T> = MutableStateFlow(initial)

    override fun updateState(block: Update<T>) {
        scope.launch(singleThreadCxt) {
            val updatedState = block.invoke(stateFlow.value)
            stateFlow.value = updatedState
        }
    }

    @Composable
    override fun observeStateUpdates(): State<T> = stateFlow.collectAsState()

    @Composable
    override fun <P> observeProperty(getter: Getter<T, P>): State<Option<P>> =
        observePropertyInternal(getter).collectAsState(none())

    private fun <P> observePropertyInternal(getter: Getter<T, P>): Flow<Option<P>> =
        stateFlow.map(getter::get).map { it.some() }
}
