package za.co.riggaroo.electricitymonitor;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ElectricityLog {

    private Long timestampOn;
    private Long timestampOff;

    public ElectricityLog(final long timestampOn, final long timestampOff) {
        this.timestampOn = timestampOn;
        this.timestampOff = timestampOff;
    }

    public ElectricityLog() {
    }

    public long getTimestampOn() {
        return timestampOn;
    }

    public void setTimestampOn(final long timestampOn) {
        this.timestampOn = timestampOn;
    }

    public Long getTimestampOff() {
        return timestampOff;
    }

    public void setTimestampOff(final Long timestampOff) {
        this.timestampOff = timestampOff;
    }
}
