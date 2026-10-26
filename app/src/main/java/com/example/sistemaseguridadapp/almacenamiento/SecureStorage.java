package com.example.sistemaseguridadapp.almacenamiento;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureStorage {
    private static final String FILE = "secure_session";
    private static final long DURACION_SESION_MS = 15 * 60 * 1000L;
    private final SharedPreferences prefs;

    public SecureStorage(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        prefs = EncryptedSharedPreferences.create(
                context,
                FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    public void saveSession(String username, String role, String token) {
        prefs.edit()
                .putString("username", username)
                .putString("role", role)
                .putString("token", token)
                .putLong("expira", System.currentTimeMillis() + DURACION_SESION_MS)
                .apply();
    }

    public boolean isSessionValid() {
        String token = prefs.getString("token", null);
        return token != null && System.currentTimeMillis() < prefs.getLong("expira", 0);
    }

    public String getUsername() { return prefs.getString("username", ""); }
    public String getRole() { return prefs.getString("role", "USER"); }
    public void clear() { prefs.edit().clear().apply(); }
}