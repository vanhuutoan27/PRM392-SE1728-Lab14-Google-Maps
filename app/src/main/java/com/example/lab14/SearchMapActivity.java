package com.example.lab14;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

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

public class SearchMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private SearchView mapSearch;

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

        // Xử lý sự kiện khi người dùng tìm kiếm địa chỉ
        mapSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (validateQuery(query)) {
                    searchLocation(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Có thể xử lý khi văn bản thay đổi nếu cần
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Thiết lập vị trí mặc định ban đầu
        LatLng defaultLocation = new LatLng(10.8437748, 106.7880926);
        googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Bamos Coffee - Lã Xuân Oai"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
    }

    // Phương thức tìm kiếm vị trí từ địa chỉ
    private void searchLocation(String query) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);

            if (addresses != null && addresses.size() > 0) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Di chuyển camera và thêm Marker
                googleMap.clear(); // Xóa các marker cũ
                googleMap.addMarker(new MarkerOptions().position(latLng).title(location.getAddressLine(0)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức validate địa chỉ
    private boolean validateQuery(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
