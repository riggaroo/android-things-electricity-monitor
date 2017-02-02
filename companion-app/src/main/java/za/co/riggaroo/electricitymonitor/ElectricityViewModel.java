package za.co.riggaroo.electricitymonitor;


import android.content.Context;
import android.content.res.Resources;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

public class ElectricityViewModel {


    private boolean isLoading;
    private final Resources resources;
    private Boolean isPowerOn = true;
    private Long timeOn;
    private Long timeOff;

    public ElectricityViewModel(Context context, final Long timeOn, final Long timeOff, boolean isPowerOn) {
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.isPowerOn = isPowerOn;
        this.resources = context.getResources();
    }

    public Long getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(final Long timeOn) {
        this.timeOn = timeOn;
    }

    public Long getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(final Long timeOff) {
        this.timeOff = timeOff;
    }

    public boolean isPowerOn() {
        return isPowerOn;
    }

    public void setIsPowerOn(boolean isPowerOn) {
        this.isPowerOn = isPowerOn;
    }

    public String elapsedTimeFormatted() {
        if (isPowerOn || timeOff == null) {
            return resources.getString(R.string.power_has_been_on_for) + " " + getDurationFormatted(timeOn);
        } else {
            return resources.getString(R.string.power_has_been_off_for) + " " + getDurationFormatted(timeOff);
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(final boolean loading) {
        isLoading = loading;
    }

    private String getDurationFormatted(final long timestamp) {
        Duration duration = getDifferenceBetweenTimeAndNow(timestamp);

        String text;
        if (duration.toMinutes() < 1) {
            text = resources.getString(R.string.few_seconds);
        } else if (duration.toMinutes() < 60) {
            text = resources.getQuantityString(R.plurals.mins_formatted, (int) duration.toMinutes(),
                    (int) duration.toMinutes());
        } else if (duration.toHours() < 24) {
            long hoursLong = duration.toHours();
            Duration minutes = duration.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            text = resources
                    .getQuantityString(R.plurals.hours_formatted, (int) hoursLong, (int) hoursLong) + ", " + resources
                    .getQuantityString(R.plurals.mins_formatted, (int) minutesElapsed, (int) minutesElapsed);
        } else {
            long days = duration.toDays();
            Duration hours = duration.minusDays(days);
            long hoursLong = hours.toHours();
            Duration minutes = hours.minusHours(hoursLong);
            long minutesElapsed = minutes.toMinutes();
            text = resources.getQuantityString(R.plurals.days_formatted, (int) days, (int) days) + ", " + resources
                    .getQuantityString(R.plurals.hours_formatted, (int) hoursLong, (int) hoursLong) + ", " + resources
                    .getQuantityString(R.plurals.mins_formatted, (int) minutesElapsed, (int) minutesElapsed);

        }

        return text;
    }

    private Duration getDifferenceBetweenTimeAndNow(long timeStart) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime otherTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStart), ZoneId.systemDefault());
        return Duration.between(otherTime, today);
    }

}
