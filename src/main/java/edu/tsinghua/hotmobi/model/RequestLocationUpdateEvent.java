package edu.tsinghua.hotmobi.model;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Arrays;

/**
 * Created by mariotaku on 16/1/29.
 */
@JsonObject
public class RequestLocationUpdateEvent extends BaseEvent {

    @JsonField(name = "calling_packages")
    String[] callingPackages;
    @JsonField(name = "battery_state")
    BatteryState batteryState;

    public BatteryState getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(BatteryState batteryState) {
        this.batteryState = batteryState;
    }

    public String[] getCallingPackages() {
        return callingPackages;
    }

    public void setCallingPackages(String[] callingPackages) {
        this.callingPackages = callingPackages;
    }

    @Override
    public String toString() {
        return "RequestLocationUpdateEvent{" +
                "callingPackages=" + Arrays.toString(callingPackages) +
                ", batteryState=" + batteryState +
                "} " + super.toString();
    }

    @NonNull
    @Override
    public String getLogFileName() {
        return "request_location_update";
    }
}
