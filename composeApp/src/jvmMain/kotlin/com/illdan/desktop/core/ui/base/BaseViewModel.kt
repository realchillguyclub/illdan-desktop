package com.illdan.desktop.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.network.base.ApiException
import com.illdan.desktop.core.util.GlobalEventManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            response.fold(
                onSuccess = { data ->
                    successCallback.invoke(data)
                },
                onFailure = { throwable ->
                    if (throwable is ApiException && throwable.code == "AUTH-002") {
                        GlobalEventManager.navigateToLogin()
                    }

                    logger.e { "에러 발생: ${throwable.stackTraceToString()}" }
                    errorCallback?.invoke(throwable)
                }
            )
        }
    }
}