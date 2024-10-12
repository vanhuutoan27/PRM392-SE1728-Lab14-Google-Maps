package com.example.lab14;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private SearchView mapSearch;
    private LatLng currentLocationLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Polyline polyline; // Biến để lưu trữ polyline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Thiết lập SearchView và bản đồ
        mapSearch = findViewById(R.id.svLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Thiết lập FusedLocationProviderClient để lấy vị trí hiện tại
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        // Xử lý sự kiện khi người dùng tìm kiếm địa chỉ
        mapSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Bắt đầu tại một vị trí mặc định nếu chưa lấy được vị trí hiện tại
        LatLng defaultLocation = new LatLng(10.8437748, 106.7880926);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
    }

    // Phương thức lấy vị trí hiện tại của người dùng
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // Đặt marker tại vị trí hiện tại và di chuyển camera
                            googleMap.addMarker(new MarkerOptions().position(currentLocationLatLng).title("Current Location"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 15));
                        } else {
                            Toast.makeText(SearchMapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Phương thức tìm kiếm vị trí từ địa chỉ
    private void searchLocation(String query) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);

            if (addresses != null && addresses.size() > 0) {
                Address location = addresses.get(0);
                LatLng destinationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Đặt marker tại vị trí đã tìm kiếm
                googleMap.clear(); // Xóa marker cũ nhưng không xóa vị trí hiện tại
                googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title(location.getAddressLine(0)));

                // Di chuyển camera đến vị trí đã tìm kiếm
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));

                // Tính khoảng cách từ vị trí hiện tại đến vị trí đã tìm kiếm
                if (currentLocationLatLng != null) {
                    float[] result = new float[1];
                    Location.distanceBetween(currentLocationLatLng.latitude, currentLocationLatLng.longitude,
                            destinationLatLng.latitude, destinationLatLng.longitude, result);

                    long distanceInKm = Math.round(result[0] / 1000);
                    Toast.makeText(this, "Distance: " + distanceInKm + " km", Toast.LENGTH_LONG).show();

                    // Nếu đã có polyline trước đó, xóa polyline cũ
                    if (polyline != null) {
                        polyline.remove();
                    }

                    // Vẽ đường nối (polyline) giữa vị trí hiện tại và vị trí đã tìm kiếm
                    polyline = googleMap.addPolyline(new PolylineOptions()
                            .add(currentLocationLatLng, destinationLatLng)
                            .width(5)
                            .color(0xFF0000FF)); // Màu xanh dương cho đường polyline
                }

            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }
}
