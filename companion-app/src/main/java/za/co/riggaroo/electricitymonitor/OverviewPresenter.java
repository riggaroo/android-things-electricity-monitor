package za.co.riggaroo.electricitymonitor;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OverviewPresenter implements OverviewContract.Presenter {
    public static final String FIREBASE_ONLINE = "online";
    public static final String FIREBASE_LOGS = "logs";
    private static final String TAG = "OverviewPresenter";

    private OverviewContract.View view;
    private ElectricityViewModel electricityViewModel;

    public OverviewPresenter(final ElectricityViewModel electricityViewModel) {
        this.electricityViewModel = electricityViewModel;
    }

    @Override
    public void attach(final OverviewContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void loadPowerInformation() {
        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference onlineRef = firebaseDatabase.child(FIREBASE_ONLINE);
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!isViewAttached()) {
                    return;
                }
                Log.d(TAG, "onDataChange: value:" + dataSnapshot.getValue() + ". Key:" + dataSnapshot.getKey());
                boolean isOnline = (boolean) dataSnapshot.getValue();
                electricityViewModel.setIsPowerOn(isOnline);
                view.updateViewModel(electricityViewModel);
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        final DatabaseReference logsRef = firebaseDatabase.child(FIREBASE_LOGS);
        logsRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!isViewAttached()) {
                    return;
                }
                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    ElectricityLog electricityLog = dataSnapshotChild.getValue(ElectricityLog.class);

                    electricityViewModel.setTimeOff(electricityLog.getTimestampOff());
                    electricityViewModel.setTimeOn(electricityLog.getTimestampOn());
                    view.updateViewModel(electricityViewModel);

                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });
    }
}
