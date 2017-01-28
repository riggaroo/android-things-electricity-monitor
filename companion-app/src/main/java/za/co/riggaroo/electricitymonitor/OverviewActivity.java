package za.co.riggaroo.electricitymonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = "OverviewActivity";
    private DatabaseReference firebaseDatabase;
    private ImageView imageViewElectricityStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        imageViewElectricityStatus = (ImageView) findViewById(R.id.image_view_electricity_status);
        final DatabaseReference onlineRef = firebaseDatabase.child("online");
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: value:" + dataSnapshot.getValue() + ". Key:" + dataSnapshot.getKey());
                boolean isOnline = (boolean) dataSnapshot.getValue();
                if (isOnline) {
                    imageViewElectricityStatus.setImageResource(R.drawable.lights_on_house);
                } else {
                    imageViewElectricityStatus.setImageResource(R.drawable.lights_off_house);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });
    }
}
