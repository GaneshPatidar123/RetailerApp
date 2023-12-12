package com.example.paypointretailer.Utils

import android.hardware.biometrics.BiometricPrompt
import org.bouncycastle.asn1.x509.qualified.BiometricData

class MyRDServiceListener : RDServiceListener {
    override fun onBiometricCaptureStarted() {
        // Handle biometric capture started event
    }

    override fun onBiometricCaptureCompleted(biometricData: BiometricData) {
        // Handle biometric capture completed event
    }

    override fun onAuthenticationSuccess(authenticationResult: BiometricPrompt.AuthenticationResult) {
        // Handle authentication success event
    }

    override fun onAuthenticationFailure(errorCode: Int, errorMessage: String) {
        // Handle authentication failure event
    }
}