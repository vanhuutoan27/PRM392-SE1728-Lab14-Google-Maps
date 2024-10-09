package com.example.lab14;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TrackMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TrackMapActivity";
    private GoogleMap googleMap;
    private SearchView svFromLocation, svToLocation;
    private LatLng fromLatLng, toLatLng;
    private Button btnCalculateDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Thiết lập SearchViews và Button
        svFromLocation = findViewById(R.id.svFromLocation);
        svToLocation = findViewById(R.id.svToLocation);
        btnCalculateDistance = findViewById(R.id.btnCalculateDistance);

        // Thiết lập bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Xử lý sự kiện khi nhấn nút "Tính khoảng cách"
        btnCalculateDistance.setOnClickListener(v -> calculateDistanceOnClick());

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Thiết lập vị trí mặc định
        LatLng defaultLocation = new LatLng(10.8437748, 106.7880926);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
    }

    // Phương thức tìm kiếm vị trí từ địa chỉ
    private void searchLocation(String query, boolean isFromLocation) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                if (isFromLocation) {
                    fromLatLng = latLng;
                    googleMap.addMarker(new MarkerOptions().position(fromLatLng).title("From: " + address.getAddressLine(0)));
                } else {
                    toLatLng = latLng;
                    googleMap.addMarker(new MarkerOptions().position(toLatLng).title("To: " + address.getAddressLine(0)));
                }

                // Di chuyển camera đến vị trí vừa tìm kiếm mà không xóa các vị trí khác
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức tính khoảng cách giữa hai vị trí khi nhấn nút
    private void calculateDistanceOnClick() {
        String fromLocation = svFromLocation.getQuery().toString();
        String toLocation = svToLocation.getQuery().toString();

        // Validate nếu người dùng chưa nhập đủ thông tin
        if (TextUtils.isEmpty(fromLocation) || TextUtils.isEmpty(toLocation)) {
            Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm kiếm vị trí xuất phát và đích
        searchLocation(fromLocation, true);
        searchLocation(toLocation, false);

        // Tính khoảng cách nếu cả hai vị trí đã được tìm thấy
        if (fromLatLng != null && toLatLng != null) {
            float[] results = new float[1];
            android.location.Location.distanceBetween(fromLatLng.latitude, fromLatLng.longitude,
                    toLatLng.latitude, toLatLng.longitude, results);
            float distanceInKm = results[0] / 1000; // Khoảng cách tính bằng km
            Toast.makeText(this, "Distance: " + distanceInKm + " km", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Unable to calculate distance. Please check the locations.", Toast.LENGTH_SHORT).show();
        }
    }
}
