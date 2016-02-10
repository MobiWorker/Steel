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

package edu.tsinghua.hotmobi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import edu.tsinghua.location.research.UploadLogsService;
import edu.tsinghua.location.research.module.R;

/**
 * Created by mariotaku on 15/8/28.
 */
public class UploadLogsPreferences extends Preference implements HotMobiConstants {

    public UploadLogsPreferences(Context context) {
        super(context);
    }

    public UploadLogsPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UploadLogsPreferences(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onClick() {
        final Context context = getContext();
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_LAST_UPLOAD_TIME).apply();
        context.startService(new Intent(context, UploadLogsService.class));
        Toast.makeText(context, R.string.uploading_started, Toast.LENGTH_SHORT).show();
    }
}
