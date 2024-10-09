package com.example.lab14;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTypeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Button btnChangeMapType;
    private int currentMapType = GoogleMap.MAP_TYPE_NORMAL; // Loại bản đồ mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút "back"
        toolbar.setNavigationOnClickListener(v -> finish());   // Khi nhấn nút "back" sẽ quay lại màn hình trước

        // Liên kết nút thay đổi loại bản đồ
        btnChangeMapType = findViewById(R.id.btnChangeMapType);

        // Lấy Google Map từ fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Thay đổi loại bản đồ khi người dùng nhấn vào nút
        btnChangeMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMapType();
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

        // Thiết lập loại bản đồ mặc định là Normal
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    // Phương thức thay đổi loại bản đồ khi nhấn vào nút
    private void changeMapType() {
        // Xoay vòng giữa các loại bản đồ khác nhau
        if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) {
            currentMapType = GoogleMap.MAP_TYPE_SATELLITE;
        } else if (currentMapType == GoogleMap.MAP_TYPE_SATELLITE) {
            currentMapType = GoogleMap.MAP_TYPE_TERRAIN;
        } else if (currentMapType == GoogleMap.MAP_TYPE_TERRAIN) {
            currentMapType = GoogleMap.MAP_TYPE_HYBRID;
        } else if (currentMapType == GoogleMap.MAP_TYPE_HYBRID) {
            currentMapType = GoogleMap.MAP_TYPE_NORMAL;
        }

        // Đặt loại bản đồ mới
        googleMap.setMapType(currentMapType);
    }
}
