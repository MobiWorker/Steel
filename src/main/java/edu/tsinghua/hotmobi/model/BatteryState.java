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
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 15/9/28.
 */
@JsonObject
public class BatteryState {
    @JsonField(name = "level")
    float level;
    @JsonField(name = "state")
    int state;

    public static BatteryState create(Context context) {
        final Context app = context.getApplicationContext();
        return create(app.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)));
    }

    public static BatteryState create(Intent intent) {
        if (intent == null) return null;
        if (!intent.hasExtra(BatteryManager.EXTRA_LEVEL) || !intent.hasExtra(BatteryManager.EXTRA_SCALE) ||
                !intent.hasExtra(BatteryManager.EXTRA_STATUS)) return null;
        final BatteryState record = new BatteryState();
        record.setLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) / (float)
                intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1));
        record.setState(intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
        return record;
    }

    @Override
    public String toString() {
        return "BatteryState{" +
                "level=" + level +
                ", state=" + state +
                '}';
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
