package com.example.fingerprintauth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

//    @Override
//    public void addContentView(View view, ViewGroup.LayoutParams params) {
//        super.addContentView(view, params);
//    }
//
//    @SuppressLint("SetTextI18n")
//    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView msg_txt = findViewById(R.id.txt_msg);
        Button login_btn = findViewById(R.id.login_btn);

        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) { // vom verifica cu switch diferitele functionalitati
            case   androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS: // asta inseamna ca putem utiliza senzorul biometric
                msg_txt.setText("You can use the fingerprint sensor to login");
                msg_txt.setTextColor(Color.parseColor("#fafafa"));
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: // asta inseamna ca dispozitivul nu are un senzor de amprenta
                msg_txt.setText("the device don't have a fingerprint sensor");
                login_btn.setVisibility(View.GONE);
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_txt.setText("the biometric sensors are currently unavailable");
                login_btn.setVisibility(View.GONE);
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_txt.setText("your device don't have any fingerprint saved, please check your security settings");
                login_btn.setVisibility(View.GONE);
                break;
        }

        // am verificat daca aputem folosi senzorii biometrici
        // acum voi crea un biometric box


        Executor executor = ContextCompat.getMainExecutor(this);

        // acum voi crea biometric prompt-ul pentru callback
        //acesta ne va da rezultatul autentificarii si daca ne putem loga

        androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(MainActivity.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            /**
             * Called when an unrecoverable error has been encountered and authentication has stopped.
             *
             * <p>After this method is called, no further events will be sent for the current
             * authentication session.
             *
             * @param errorCode An integer ID associated with the error.
             * @param errString A human-readable string that describes the error.
             */

            //  metoda asta este called cand este o eroare la autentificare
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            /**
             * Called when a biometric (e.g. fingerprint, face, etc.) is recognized, indicating that the
             * user has successfully authenticated.
             *
             * <p>After this method is called, no further events will be sent for the current
             * authentication session.
             *
             * @param result An object containing authentication-related data.
             */
            // metoda asta este called cand autentificarea a mers
            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when a biometric (e.g. fingerprint, face, etc.) is presented but not recognized as
             * belonging to the user.
             */
            // metoda asta este cand a failuit autentificarea
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        // biometric Dialog
        //androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
        androidx.biometric.BiometricPrompt.PromptInfo.Builder builder = new androidx.biometric.BiometricPrompt.PromptInfo.Builder();
        builder.setTitle("Login");
        builder.setDescription("Use your fingerprint to login to your app");
        builder.setNegativeButtonText("Cancel");
        androidx.biometric.BiometricPrompt.PromptInfo promptInfo = builder
                .build();

        // call the dialog cand este apasat butonul de login

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

    }
}