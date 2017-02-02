package za.co.riggaroo.electricitymonitor;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.jakewharton.threetenabp.AndroidThreeTen;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ElectricityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder().setDefaultFontPath("minyna.ttf").setFontAttrId(R.attr.fontPath)
                        .build());
        AndroidThreeTen.init(this);
        FirebaseApp.initializeApp(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
