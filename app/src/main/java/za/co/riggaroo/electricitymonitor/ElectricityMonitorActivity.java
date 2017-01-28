package za.co.riggaroo.electricitymonitor;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ElectricityMonitorActivity extends AppCompatActivity {
    public static final String FIREBASE_LOGS = "logs";
    private static final String TAG = "ElectricityMonitor";
    private DatabaseReference firebaseDatabase;
    private ElectricityLog electricityLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_monitor);
        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        String key = firebaseDatabase.child(FIREBASE_LOGS).push().getKey();

        electricityLog = new ElectricityLog();
        electricityLog.setTimestampOn(System.currentTimeMillis());
        final DatabaseReference currentLogDbRef = firebaseDatabase.child(FIREBASE_LOGS).child(key);
        currentLogDbRef.setValue(electricityLog);


        final DatabaseReference onlineRef = firebaseDatabase.child(".info/connected");
        final DatabaseReference currentUserRef = firebaseDatabase.child("/online");
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot:" + dataSnapshot);
                if (dataSnapshot.getValue(Boolean.class)) {
                    currentUserRef.setValue(true);
                    currentUserRef.onDisconnect().setValue(false);

                    electricityLog.setTimestampOff(System.currentTimeMillis());
                    currentLogDbRef.onDisconnect().setValue(electricityLog, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(final DatabaseError databaseError,
                                               final DatabaseReference databaseReference) {
                            Log.d(TAG, "onComplete listener, disconnect");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError:" + databaseError);
            }
        });
    }
}
