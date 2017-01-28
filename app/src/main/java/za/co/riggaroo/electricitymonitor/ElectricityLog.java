package za.co.riggaroo.electricitymonitor;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class ElectricityLog {

    private Map<String, String> timestampOn;
    private Map<String, String> timestampOff;


    public ElectricityLog() {
    }

    public Map<String, String> getTimestampOn() {
        return timestampOn;
    }

    public void setTimestampOn(final Map<String, String> timestampOn) {
        this.timestampOn = timestampOn;
    }

    public Map<String, String> getTimestampOff() {
        return timestampOff;
    }

    public void setTimestampOff(final Map<String, String> timestampOff) {
        this.timestampOff = timestampOff;
    }
}
