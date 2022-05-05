package com.app.blocodenotas

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.blocodenotas.databinding.ActivityMainBinding
import timber.log.Timber
import javax.crypto.Cipher
import kotlin.coroutines.coroutineContext

fun Fragment.promptBiometricChecker(
    title: String,
    message: String? = null, // OPCIONAL - SE QUISER EXIBIR UMA MENSAGEM
    negativeLabel: String,
    confirmationRequired: Boolean = true,
    initializedCipher: Cipher? = null, // OPICIONAL - SE VC MESMO(SUA APP) QUISER MANTER O CONTROLE SOBRE OS ACESSOS
    onAuthenticationSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    onAuthenticationError: (Int, String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)
    val prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            Timber.d("Authenticado com sucesso, acesso permitido!")
            onAuthenticationSuccess(result)
        }

        override fun onAuthenticationError(errorCode: Int, errorMessage: CharSequence) {
            Timber.d("Acesso negado! Alguem ta tentando usar teu celular!")
            onAuthenticationError(errorCode, errorMessage.toString())
        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .apply { if (message != null) setDescription(message) }
        .setConfirmationRequired(confirmationRequired)
        .setNegativeButtonText(negativeLabel)
        .build()

    initializedCipher?.let {
        val cryptoObject = BiometricPrompt.CryptoObject(initializedCipher)
        prompt.authenticate(promptInfo, cryptoObject)
        return
    }

    prompt.authenticate(promptInfo)
}