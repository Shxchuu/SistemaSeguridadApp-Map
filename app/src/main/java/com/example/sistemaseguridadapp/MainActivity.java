package com.example.sistemaseguridadapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sistemaseguridadapp.almacenamiento.SecureStorage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng OVALLE = new LatLng(-30.5983, -71.2003);

    private GoogleMap mMap;
    private SecureStorage storage;

    private MaterialCardView cardImagePreview;
    private ImageView imgPreview;
    private FloatingActionButton btnTakePhoto;
    private FloatingActionButton btnMyLocation;
    private FloatingActionButton btnClearMarkers;
    private ImageButton btnClosePreview;
    private Button btnCerrarSesion;
    private TextView tvTitulo;

    private int markerCount = 1;
    private Marker ovalleMarker;
    private final List<Marker> userMarkers = new ArrayList<>();

    // Launchers de Permisos y Cámara
    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if ((fineGranted != null && fineGranted) || (coarseGranted != null && coarseGranted)) {
                    enableMyLocationOnMap();
                    checkLocationPermissionAndGetLocation();
                } else {
                    Toast.makeText(this, "Permiso de ubicación necesario", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "Permiso de cámara necesario", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.get("data") != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imgPreview.setImageBitmap(imageBitmap);
                        cardImagePreview.setVisibility(View.VISIBLE);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Validación de seguridad de sesión existente
        try {
            storage = new SecureStorage(this);
        } catch (Exception e) {
            irALogin();
            return;
        }
        if (!storage.isSessionValid()) {
            storage.clear();
            irALogin();
            return;
        }

        setContentView(R.layout.activity_main);

        // Inicialización de UI
        tvTitulo = findViewById(R.id.tvTitulo);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        cardImagePreview = findViewById(R.id.cardImagePreview);
        imgPreview = findViewById(R.id.imgPreview);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnClearMarkers = findViewById(R.id.btnClearMarkers);
        btnClosePreview = findViewById(R.id.btnClosePreview);

        tvTitulo.setText("Seguro - " + storage.getUsername());

        // Configuración del fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Listeners de botones
        btnCerrarSesion.setOnClickListener(v -> {
            storage.clear();
            irALogin();
        });

        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndLaunch());
        btnMyLocation.setOnClickListener(v -> checkLocationPermissionAndGetLocation());

        btnClearMarkers.setOnClickListener(v -> {
            if (mMap != null) {
                for (Marker marker : userMarkers) {
                    marker.remove();
                }
                userMarkers.clear();
                markerCount = 1;

                if (ovalleMarker == null) {
                    addOvalleMarker();
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OVALLE, 14f));
                Toast.makeText(this, "Marcadores eliminados. Manteniendo Ovalle.", Toast.LENGTH_SHORT).show();
            }
        });

        btnClosePreview.setOnClickListener(v -> cardImagePreview.setVisibility(View.GONE));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configuración de controles del mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Añadir marcador principal en Ovalle
        addOvalleMarker();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OVALLE, 14f));

        // Eventos para agregar marcadores tocando el mapa
        mMap.setOnMapClickListener(this::addPointMarker);
        mMap.setOnMapLongClickListener(this::addPointMarker);

        // Gestión de clics en la ventanita del marcador
        mMap.setOnInfoWindowClickListener(marker -> {
            if (marker.equals(ovalleMarker)) {
                Toast.makeText(this, "Ubicación Principal: Ovalle, Chile", Toast.LENGTH_SHORT).show();
            } else {
                userMarkers.remove(marker);
                marker.remove();
                Toast.makeText(this, "Marcador eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        checkLocationPermissionAndGetLocation();
    }

    private void addPointMarker(@NonNull LatLng latLng) {
        if (mMap == null) return;

        String title = "Punto #" + markerCount++;
        String snippet = String.format(Locale.getDefault(), "Lat: %.5f, Lng: %.5f", latLng.latitude, latLng.longitude);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        if (marker != null) {
            userMarkers.add(marker);
            marker.showInfoWindow();
        }

        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }

    private void addOvalleMarker() {
        if (mMap == null) return;
        if (ovalleMarker != null) {
            ovalleMarker.remove();
        }
        ovalleMarker = mMap.addMarker(new MarkerOptions()
                .position(OVALLE)
                .title("Ovalle, Chile")
                .snippet("Ubicación Principal (Región de Coquimbo)")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if (ovalleMarker != null) {
            ovalleMarker.showInfoWindow();
        }
    }

    private void checkLocationPermissionAndGetLocation() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OVALLE, 14f));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            enableMyLocationOnMap();
        }
    }

    private void enableMyLocationOnMap() {
        if (mMap != null) {
            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            cameraLauncher.launch(takePictureIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private void irALogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}