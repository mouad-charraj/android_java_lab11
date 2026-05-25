package com.example.lab11v2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MapView mouad_mapView;
    private Marker mouad_userMarker;
    private final ArrayList<Marker> mouad_pointsList = new ArrayList<>();
    private Location mouad_lastLoc;

    @Override
    protected void onCreate(Bundle mouad_savedState) {
        super.onCreate(mouad_savedState);

        Configuration.getInstance().load(this, getSharedPreferences("mouad_prefs", MODE_PRIVATE));
        setContentView(R.layout.activity_main);

        mouad_mapView = findViewById(R.id.map_mouad);
        mouad_mapView.setMultiTouchControls(true);
        mouad_mapView.getController().setZoom(15.0);

        FloatingActionButton mouad_fab = findViewById(R.id.fab_center_mouad);
        mouad_fab.setOnClickListener(mouad_v -> {
            if (mouad_lastLoc != null) {
                GeoPoint mouad_p = new GeoPoint(mouad_lastLoc.getLatitude(), mouad_lastLoc.getLongitude());
                mouad_mapView.getController().animateTo(mouad_p);
                mouad_mapView.getController().setZoom(18.0);
            } else {
                Toast.makeText(this, "Position introuvable", Toast.LENGTH_SHORT).show();
            }
        });

        mouad_initMapEvents();

        LocationManager mouad_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mouad_startTracking(mouad_manager);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private void mouad_initMapEvents() {
        MapEventsReceiver mouad_receiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint mouad_gp) {
                Marker mouad_m = new Marker(mouad_mapView);
                mouad_m.setPosition(mouad_gp);
                mouad_m.setTitle("Point Mouad");
                mouad_m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mouad_m.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.marker_default, null));
                
                mouad_mapView.getOverlays().add(mouad_m);
                mouad_pointsList.add(mouad_m);
                mouad_mapView.invalidate();
                
                Toast.makeText(MainActivity.this, "Marqueur ajouté", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint mouad_gp) {
                for (Marker mouad_item : mouad_pointsList) {
                    mouad_mapView.getOverlays().remove(mouad_item);
                }
                mouad_pointsList.clear();
                mouad_mapView.invalidate();
                Toast.makeText(MainActivity.this, "Carte vidée", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        mouad_mapView.getOverlays().add(new MapEventsOverlay(mouad_receiver));
    }

    private void mouad_startTracking(LocationManager mouad_lm) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

        LocationListener mouad_listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location mouad_location) {
                mouad_lastLoc = mouad_location;
                GeoPoint mouad_point = new GeoPoint(mouad_location.getLatitude(), mouad_location.getLongitude());

                if (mouad_userMarker == null) {
                    mouad_userMarker = new Marker(mouad_mapView);
                    mouad_userMarker.setTitle("Mouad");
                    mouad_userMarker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.person, null));
                    mouad_mapView.getOverlays().add(mouad_userMarker);
                    mouad_mapView.getController().animateTo(mouad_point);
                }
                mouad_userMarker.setPosition(mouad_point);
                mouad_mapView.invalidate();
            }

            @Override
            public void onProviderDisabled(@NonNull String mouad_provider) {
                if (LocationManager.GPS_PROVIDER.equals(mouad_provider)) {
                    mouad_showGpsDialog();
                }
            }
        };

        mouad_lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, mouad_listener);
        mouad_lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, mouad_listener);
    }

    private void mouad_showGpsDialog() {
        new AlertDialog.Builder(this)
                .setMessage("GPS désactivé. L'activer ?")
                .setPositiveButton("Oui", (mouad_d, mouad_id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int mouad_reqCode, @NonNull String[] mouad_perms, @NonNull int[] mouad_results) {
        super.onRequestPermissionsResult(mouad_reqCode, mouad_perms, mouad_results);
        if (mouad_reqCode == 200 && mouad_results.length > 0 && mouad_results[0] == PackageManager.PERMISSION_GRANTED) {
            mouad_startTracking((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        }
    }

    @Override
    protected void onResume() { super.onResume(); mouad_mapView.onResume(); }
    @Override
    protected void onPause() { super.onPause(); mouad_mapView.onPause(); }
}
