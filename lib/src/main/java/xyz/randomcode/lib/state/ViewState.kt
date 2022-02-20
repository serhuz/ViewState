package xyz.randomcode.lib.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import arrow.core.Tuple4
import arrow.core.Tuple5
import arrow.optics.Getter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

typealias Update<State> = State.() -> State

abstract class ViewState<T>(protected val initial: T) {

    abstract val current: T

    abstract fun updateState(block: Update<T>)

    @Composable
    abstract fun observeStateUpdates(): State<T>

    @Composable
    abstract fun <P> observe(getter: Getter<T, P>): State<P>

    @Composable
    abstract fun <P1, P2> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>
    ): State<Pair<P1, P2>>

    @Composable
    abstract fun <P1, P2, P3> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>
    ): State<Triple<P1, P2, P3>>

    @Composable
    abstract fun <P1, P2, P3, P4> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>,
        getterP4: Getter<T, P4>
    ): State<Tuple4<P1, P2, P3, P4>>

    @Composable
    abstract fun <P1, P2, P3, P4, P5> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>,
        getterP4: Getter<T, P4>,
        getterP5: Getter<T, P5>,
    ): State<Tuple5<P1, P2, P3, P4, P5>>
}

class FlowViewState<T>(initial: T, private val scope: CoroutineScope) : ViewState<T>(initial) {

    private val singleThreadCxt: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val stateFlow: MutableStateFlow<T> = MutableStateFlow(initial)

    override val current: T
        get() = stateFlow.value

    override fun updateState(block: Update<T>) {
        scope.launch(singleThreadCxt) {
            val updatedState = block.invoke(stateFlow.value)
            stateFlow.value = updatedState
        }
    }

    @Composable
    override fun observeStateUpdates(): State<T> = stateFlow.collectAsState()

    @Composable
    override fun <P> observe(getter: Getter<T, P>): State<P> =
        observeProperty(getter).collectAsState(getter.get(initial))

    @Composable
    override fun <P1, P2> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>
    ): State<Pair<P1, P2>> = combine(observeProperty(getterP1), observeProperty(getterP2), ::Pair)
        .collectAsState(getterP1.get(initial) to getterP2.get(initial))

    @Composable
    override fun <P1, P2, P3> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>
    ): State<Triple<P1, P2, P3>> = combine(
        observeProperty(getterP1),
        observeProperty(getterP2),
        observeProperty(getterP3),
        ::Triple
    ).collectAsState(Triple(getterP1.get(initial), getterP2.get(initial), getterP3.get(initial)))

    private fun <P> observeProperty(getter: Getter<T, P>): Flow<P> =
        stateFlow.map(getter::get)

    @Composable
    override fun <P1, P2, P3, P4> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>,
        getterP4: Getter<T, P4>
    ): State<Tuple4<P1, P2, P3, P4>> = combine(
        observeProperty(getterP1),
        observeProperty(getterP2),
        observeProperty(getterP3),
        observeProperty(getterP4),
        ::Tuple4
    ).collectAsState(
        Tuple4(
            getterP1.get(initial),
            getterP2.get(initial),
            getterP3.get(initial),
            getterP4.get(initial)
        )
    )

    @Composable
    override fun <P1, P2, P3, P4, P5> observe(
        getterP1: Getter<T, P1>,
        getterP2: Getter<T, P2>,
        getterP3: Getter<T, P3>,
        getterP4: Getter<T, P4>,
        getterP5: Getter<T, P5>
    ): State<Tuple5<P1, P2, P3, P4, P5>> = combine(
        observeProperty(getterP1),
        observeProperty(getterP2),
        observeProperty(getterP3),
        observeProperty(getterP4),
        observeProperty(getterP5),
        ::Tuple5
    ).collectAsState(
        Tuple5(
            getterP1.get(initial),
            getterP2.get(initial),
            getterP3.get(initial),
            getterP4.get(initial),
            getterP5.get(initial)
        )
    )
}
