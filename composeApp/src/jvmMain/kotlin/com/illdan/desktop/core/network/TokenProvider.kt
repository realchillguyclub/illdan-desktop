package com.illdan.desktop.core.network

import com.illdan.desktop.domain.model.auth.AuthTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Properties

class TokenProvider {
    private val storeDir: Path = Path.of(System.getProperty("user.home"), ".illdan")
    private val fileName: String = "tokens.properties"
    private val mutex = Mutex()
    private val filePath: Path = storeDir.resolve(fileName)

    suspend fun getToken(): AuthTokens? = mutex.withLock {
        withContext(Dispatchers.IO) {
            if (!Files.exists(filePath)) return@withContext null
            val props = Properties().apply {
                Files.newInputStream(filePath).use { load(it) }
            }
            val access = props.getProperty(KEY_ACCESS)?.takeIf { it.isNotBlank() }
            val refresh = props.getProperty(KEY_REFRESH)?.takeIf { it.isNotBlank() }
            if (access != null && refresh != null) AuthTokens(access, refresh) else null
        }
    }

    suspend fun saveToken(newToken: AuthTokens) = mutex.withLock {
        withContext(Dispatchers.IO) {
            Files.createDirectories(storeDir)

            val props = Properties().apply {
                setProperty(KEY_ACCESS, newToken.accessToken)
                setProperty(KEY_REFRESH, newToken.refreshToken)
            }

            val tmp = Files.createTempFile(storeDir, "tokens", ".tmp")
            Files.newOutputStream(tmp).use { props.store(it, "OnDot Desktop Tokens") }
            Files.move(tmp, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
        }
    }

    suspend fun clearToken() = mutex.withLock {
        withContext(Dispatchers.IO) {
            if (Files.exists(filePath)) {
                try {
                    Files.delete(filePath)
                } catch (_: Exception) {
                    val props = Properties()
                    Files.newOutputStream(filePath).use { props.store(it, "Cleared") }
                }
            }
        }
    }

    private companion object {
        const val KEY_ACCESS = "access"
        const val KEY_REFRESH = "refresh"
    }
}