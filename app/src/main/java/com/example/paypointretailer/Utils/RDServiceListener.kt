package com.example.paypointretailer.Utils

import android.hardware.biometrics.BiometricPrompt
import org.bouncycastle.asn1.x509.qualified.BiometricData

interface RDServiceListener {
    fun onBiometricCaptureStarted()

    // Event triggered when biometric capture is completed
    fun onBiometricCaptureCompleted(biometricData: BiometricData)

    // Event triggered when authentication is successful
    fun onAuthenticationSuccess(authenticationResult: BiometricPrompt.AuthenticationResult)

    // Event triggered when authentication fails
    fun onAuthenticationFailure(errorCode: Int, errorMessage: String)
}