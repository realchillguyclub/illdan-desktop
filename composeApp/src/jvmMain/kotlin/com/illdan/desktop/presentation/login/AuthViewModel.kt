package com.illdan.desktop.presentation.login

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.core.util.BrowserManager
import com.illdan.desktop.domain.model.auth.AuthInfo
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(
    private val authRepository: AuthRepository,
): BaseViewModel<UiState.Default>(UiState.Default) {
    private val logger = Logger.withTag("AuthViewModel")
    private var state = ""
    private var pollingJob: Job? = null

    fun kakaoLogin() {
        viewModelScope.launch {
            authRepository.getAuthUrl().collect {
                resultResponse(it, ::onSuccessGetAuthUrl)
            }
        }
    }

    private fun onSuccessGetAuthUrl(result: AuthUrl) {
        logger.d { "onSuccessGetAuthUrl: $result" }
        state = result.state
        BrowserManager.open(result.authorizeUrl)

        startPollingAuthInfo(
            timeoutMillis = 90_000L, // 90초
            intervalMillis = 1_500L  // 1.5초
        )
    }

    /** 폴링 제어: 시작/중단 */
    fun startPollingAuthInfo(
        timeoutMillis: Long = 90_000L,
        intervalMillis: Long = 1_500L
    ) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            try {
                withTimeout(timeoutMillis) {
                    while (isActive) {
                        val success = getAuthInfo()
                        if (success) {
                            logger.i { "Auth polling 성공 polling 중단" }
                            break
                        }
                        delay(intervalMillis)
                    }
                }
            } catch (t: TimeoutCancellationException) {
                logger.w { "Auth polling timeout ${timeoutMillis}ms" }
            } catch (t: CancellationException) {
                // 정상 취소
            } catch (t: Throwable) {
                logger.e(t) { "Auth polling 실패 알 수 없는 에러" }
            } finally {
                pollingJob = null
            }
        }
    }

    fun stopPollingAuthInfo() {
        pollingJob?.cancel()
        pollingJob = null
    }

    /** 단일 조회 시도: 성공 시 true, 아직이면 false */
    private suspend fun getAuthInfo(): Boolean {
        var done = false

        authRepository.getAuthInfo(state).collect {
            resultResponse(
                response = it,
                successCallback = {
                    done = true
                    onSuccessGetAuthInfo(it)
                },
                errorCallback = {
                    logger.d { "Auth not ready yet: ${it.message}" }
                }
            )
        }
        return done
    }

    /** 최종 성공 처리 */
    private fun onSuccessGetAuthInfo(result: AuthInfo) {
        viewModelScope.launch {
            authRepository.saveToken(result.authToken)
            stopPollingAuthInfo()
            emitEventFlow(AuthEvent.NavigateToMain)
            logger.i { "Auth success: $result" }
        }
    }
}