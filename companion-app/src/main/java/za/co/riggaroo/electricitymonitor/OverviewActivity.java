package za.co.riggaroo.electricitymonitor;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = "OverviewActivity";
    private DatabaseReference firebaseDatabase;
    private ImageView imageViewElectricityStatus;
    private ConstraintLayout constraintLayout;
    private TextView textViewOverallStatus, textViewTimeElapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        setupLayout();
        setupDatabase();
    }


    private void setupLayout() {
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_container);
        imageViewElectricityStatus = (ImageView) findViewById(R.id.image_view_electricity_status);
        textViewOverallStatus = (TextView) findViewById(R.id.text_view_overall_status);
        textViewTimeElapsed = (TextView) findViewById(R.id.text_view_time_elapsed);
    }

    private void setupDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference onlineRef = firebaseDatabase.child("online");
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: value:" + dataSnapshot.getValue() + ". Key:" + dataSnapshot.getKey());
                boolean isOnline = (boolean) dataSnapshot.getValue();
                toggleLayout(isOnline);
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        final DatabaseReference logsRef = firebaseDatabase.child("logs");
        logsRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //  Log.d(TAG, "logsRef - onDataChange:" + dataSnapshot.getValue().toString());
                calculateElapsedTimeInCurrentState(dataSnapshot);
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });
    }

    private void calculateElapsedTimeInCurrentState(final DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
            ElectricityLog electricityLog = dataSnapshotChild.getValue(ElectricityLog.class);
            Log.d(TAG, "Electricity log:" + electricityLog.getTimestampOn() + ". Timestamp off:" + electricityLog
                    .getTimestampOff());
            if (electricityLog.getTimestampOff() == null) {
                setElectricityOnText(electricityLog.getTimestampOn());
            } else {
                setElectricityOffStatusText(electricityLog.getTimestampOff());
            }

        }
    }

    private void setElectricityOffStatusText(final long timestampOff) {
        org.threeten.bp.Duration duration = getDifferenceBetweenTimeAndNow(timestampOff);
        Log.d(TAG, "duration off:" + duration.toString());

        if (duration.toMinutes() < 60) {
            textViewTimeElapsed.setText(getResources()
                    .getQuantityString(R.plurals.time_off_formatted, (int) duration.toMinutes(),
                            (int) duration.toMinutes()));
        } else if (duration.toHours() < 24) {
            long hoursLong = duration.toHours();
            Duration minutes = duration.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            textViewTimeElapsed.setText(getString(R.string.time_off_formatted_hours_min, hoursLong, minutesElapsed));
        } else {
            long days = duration.toDays();
            Duration hours = duration.minusDays(days);
            long hoursLong = hours.toHours();
            Duration minutes = hours.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            textViewTimeElapsed
                    .setText(getString(R.string.time_off_formatted_days_hours_min, days, hoursLong, minutesElapsed));
        }
    }

    private void setElectricityOnText(final long timestampOn) {
        org.threeten.bp.Duration duration = getDifferenceBetweenTimeAndNow(timestampOn);
        Log.d(TAG, "duration on:" + duration.toString());
        if (duration.toMinutes() < 60) {
            textViewTimeElapsed.setText(getResources()
                    .getQuantityString(R.plurals.time_on_formatted, (int) duration.toMinutes(),
                            (int) duration.toMinutes()));
        } else if (duration.toHours() < 24) {
            long hoursLong = duration.toHours();
            Duration minutes = duration.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            textViewTimeElapsed.setText(getString(R.string.time_on_formatted_hours_min, hoursLong, minutesElapsed));
        } else {
            long days = duration.toDays();
            Duration hours = duration.minusDays(days);
            long hoursLong = hours.toHours();
            Duration minutes = hours.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            textViewTimeElapsed
                    .setText(getString(R.string.time_on_formatted_days_hours_min, days, hoursLong, minutesElapsed));
        }

    }

    private org.threeten.bp.Duration getDifferenceBetweenTimeAndNow(long timeStart) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime otherTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStart), ZoneId.systemDefault());
        org.threeten.bp.Duration diff = org.threeten.bp.Duration.between(otherTime, today);
        Log.d(TAG, "Min between " + diff.toMinutes());
        return diff;
    }

    private void toggleLayout(final boolean isOnline) {
        if (isOnline) {
            imageViewElectricityStatus.setImageResource(R.drawable.lights_on_house);
            constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_on_background));
            textViewOverallStatus.setText(getString(R.string.power_is_on));
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_on_background_darker));

        } else {
            imageViewElectricityStatus.setImageResource(R.drawable.lights_off_house);
            constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_off_background));
            textViewOverallStatus.setText(getString(R.string.power_is_off));
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.color_lights_off_background_darker));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
