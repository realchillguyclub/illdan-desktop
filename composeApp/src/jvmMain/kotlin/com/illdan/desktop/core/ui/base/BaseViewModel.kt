package com.illdan.desktop.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.util.GlobalEventManager
import com.illdan.desktop.domain.error.DomainError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel<STATE : UiState>(
    initialState: STATE,
) : ViewModel() {
    private val logger = Logger.withTag(tag = "BaseViewModel")

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()
    protected val currentState: STATE get() = _uiState.value

    private val _eventFlow =
        MutableSharedFlow<Event>(
            replay = 0,
            extraBufferCapacity = 64,
        )
    val eventFlow = _eventFlow.asSharedFlow()

    protected fun updateState(state: STATE) {
        viewModelScope.launch {
            _uiState.update { state }
        }
    }

    protected fun updateStateSync(state: STATE) {
        _uiState.update { state }
    }

    protected suspend fun emitEventFlow(event: Event) {
        _eventFlow.emit(event)
    }

    protected fun tryEmitEventFlow(event: Event) {
        _eventFlow.tryEmit(event)
    }

    protected fun <D> launchResult(
        block: suspend () -> Result<D>,
        onSuccess: (D) -> Unit,
        onError: (Throwable) -> Unit = {},
    ) {
        viewModelScope.launch {
            val result =
                try {
                    block()
                } catch (e: Exception) {
                    Result.failure(e)
                }

            result.fold(
                onSuccess = onSuccess,
                onFailure = { t ->
                    if (t is CancellationException) throw t
                    logger.e(t) { "에러가 발생했습니다: ${t.message}" }
                    handleGlobalError(t)
                    onError(t)
                },
            )
        }
    }

    protected suspend fun handleGlobalError(t: Throwable) {
        when (t) {
            is DomainError.AuthExpired,
            is DomainError.UnKnownUser,
            -> GlobalEventManager.navigateToLogin()
        }
    }
}
