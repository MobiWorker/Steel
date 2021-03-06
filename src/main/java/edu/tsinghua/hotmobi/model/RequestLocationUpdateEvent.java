package edu.tsinghua.hotmobi.model;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/1/29.
 */
@JsonObject
public class RequestLocationUpdateEvent extends BaseEvent {

    @Override
    public String toString() {
        return "RequestLocationUpdateEvent{} " + super.toString();
    }

    @NonNull
    @Override
    public String getLogFileName() {
        return "request_location_update";
    }
}
