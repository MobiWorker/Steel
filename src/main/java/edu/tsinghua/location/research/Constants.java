package edu.tsinghua.location.research;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by mariotaku on 16/1/29.
 */
public interface Constants {
    String LOGTAG = "LocResearch";
    Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority("edu.tsinghua.location.research.module").build();
}
