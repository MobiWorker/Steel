package edu.tsinghua.location.research;

import android.content.ContentResolver;
import android.net.Uri;

import edu.tsinghua.location.research.module.BuildConfig;

/**
 * Created by mariotaku on 16/1/29.
 */
public interface Constants {
    String LOGTAG = "LocResearch";
    Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.APPLICATION_ID).build();
}
