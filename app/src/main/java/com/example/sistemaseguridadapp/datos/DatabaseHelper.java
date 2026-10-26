package com.example.sistemaseguridadapp.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import com.example.sistemaseguridadapp.utilidades.SecurityUtils;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "seguridad.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "salt TEXT NOT NULL,"
                + "hash TEXT NOT NULL,"
                + "rol TEXT NOT NULL DEFAULT 'USER')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public boolean registrarUsuario(String username, String password) {
        byte[] salt = SecurityUtils.newSalt();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("salt", Base64.encodeToString(salt, Base64.NO_WRAP));
        v.put("hash", SecurityUtils.hashPassword(password, salt));
        v.put("rol", "USER");
        return getWritableDatabase().insert("usuarios", null, v) != -1;
    }

    public String validarLogin(String username, String password) {
        try (Cursor c = getReadableDatabase().rawQuery(
                "SELECT salt, hash, rol FROM usuarios WHERE username = ?",
                new String[]{username})) {
            if (!c.moveToFirst()) return null;
            byte[] salt = Base64.decode(c.getString(0), Base64.NO_WRAP);
            String hashGuardado = c.getString(1);
            String hashIngresado = SecurityUtils.hashPassword(password, salt);
            if (SecurityUtils.constantTimeEquals(hashGuardado, hashIngresado)) {
                return c.getString(2);
            }
            return null;
        }
    }
}