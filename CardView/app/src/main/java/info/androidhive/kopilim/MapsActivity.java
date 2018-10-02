package info.androidhive.kopilim;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLv = 16.0f;

        Bundle bundle = getIntent().getExtras();
        double latti = bundle.getDouble("latt");
        double longgi = bundle.getDouble("longt");

        String marker = null;
        if(latti == 6.46284){
            marker = "UUM Library";
        }else if(latti == 6.4635654){
            marker = "UUM CoB";
        }else if(latti == 6.4678949){
            marker = "UUM CaS";
        }else if(latti == 6.4578851){
            marker = "UUM CoLGiS";
        }else if(latti == 6.461182){
            marker = "UUM U-Assist";
        }

        LatLng location = new LatLng(latti, longgi);
        mMap.addMarker(new MarkerOptions().position(location).title(marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLv));
        Toast.makeText(this, "CLICK on the Marker to Check the Location",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
