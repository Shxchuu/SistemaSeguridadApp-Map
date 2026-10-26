package com.example.sistemaseguridadapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemaseguridadapp.almacenamiento.SecureStorage;
import com.example.sistemaseguridadapp.datos.DatabaseHelper;
import com.example.sistemaseguridadapp.utilidades.SecurityUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsuario, etPassword;
    private TextView tvMensaje;
    private DatabaseHelper db;
    private SecureStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        tvMensaje = findViewById(R.id.tvMensaje);

        try {
            storage = new SecureStorage(this);
        } catch (Exception e) {
            Toast.makeText(this, "Error de almacenamiento seguro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        db = new DatabaseHelper(this);

        if (storage.isSessionValid()) {
            irAMain();
            return;
        }

        findViewById(R.id.btnLogin).setOnClickListener(v -> iniciarSesion());
        findViewById(R.id.btnRegistro).setOnClickListener(v -> registrar());
    }

    private void iniciarSesion() {
        String usuario = SecurityUtils.sanitize(etUsuario.getText().toString());
        String pass = etPassword.getText().toString();

        if (!SecurityUtils.isValidUsername(usuario) || pass.isEmpty()) {
            mostrar("Usuario o contraseña inválidos.");
            return;
        }

        String rol = db.validarLogin(usuario, pass);
        if (rol == null) {
            mostrar("Credenciales incorrectas.");
            return;
        }

        storage.saveSession(usuario, rol, SecurityUtils.newToken());
        etPassword.setText("");
        irAMain();
    }

    private void registrar() {
        String usuario = SecurityUtils.sanitize(etUsuario.getText().toString());
        String pass = etPassword.getText().toString();

        if (!SecurityUtils.isValidUsername(usuario)) {
            mostrar("Usuario: 4 a 20 caracteres (letras, números o _).");
            return;
        }
        if (!SecurityUtils.isStrongPassword(pass)) {
            mostrar("Contraseña débil (mín. 8 caracteres, mayúscula, minúscula y número).");
            return;
        }
        if (db.registrarUsuario(usuario, pass)) {
            etPassword.setText("");
            mostrar("Usuario registrado con éxito.");
        } else {
            mostrar("El usuario ya existe.");
        }
    }

    private void mostrar(String msg) { tvMensaje.setText(msg); }
    private void irAMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}