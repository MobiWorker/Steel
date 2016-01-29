/*
 *                 Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2015 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.tsinghua.hotmobi.model;

import android.content.Context;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.TimeZone;

import edu.tsinghua.hotmobi.util.LocationUtils;
import edu.tsinghua.location.research.module.BuildConfig;

/**
 * Created by mariotaku on 15/8/8.
 */
@JsonObject
public abstract class BaseEvent implements LogModel {

    @JsonField(name = "app_version")
    int appVersion = BuildConfig.VERSION_CODE;

    @JsonField(name = "start_time")
    long startTime;

    @JsonField(name = "end_time")
    long endTime;

    @JsonField(name = "time_offset")
    long timeOffset;

    @JsonField(name = "location")
    LatLng location;

    public BaseEvent() {
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void markStart(Context context) {
        setStartTime(System.currentTimeMillis());
        setTimeOffset(TimeZone.getDefault().getOffset(startTime));
        setLocation(LocationUtils.getCachedLatLng(context));
    }

    public void markEnd() {
        setEndTime(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "appVersion=" + appVersion +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeOffset=" + timeOffset +
                ", location=" + location +
                '}';
    }

}
