package com.illdan.desktop.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.network.base.ApiException
import com.illdan.desktop.core.util.GlobalEventManager
import com.illdan.desktop.domain.error.DomainError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel<STATE: UiState>(
    initialState: STATE
): ViewModel() {
    private val logger = Logger.withTag(tag = "BaseViewModel")

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    protected fun updateState(state: STATE) {
        viewModelScope.launch {
            _uiState.update { state }
        }
    }

    protected fun updateStateSync(state: STATE) {
        _uiState.update { state }
    }

    protected fun emitEventFlow(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected fun<D> resultResponse(
        response: Result<D>,
        successCallback: (D) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        response.fold(
            onSuccess = { data ->
                successCallback.invoke(data)
            },
            onFailure = { throwable ->
                // CancellationException은 코루틴 취소 신호이므로 그대로 전파해 상위 코루틴이 취소되도록 한다
                if (throwable is CancellationException) throw throwable

                when(throwable) {
                    is DomainError.AuthExpired -> viewModelScope.launch { GlobalEventManager.navigateToLogin() }
                }

                logger.e { "에러 발생: ${throwable.stackTraceToString()}" }
                errorCallback?.invoke(throwable)
            }
        )
    }
}