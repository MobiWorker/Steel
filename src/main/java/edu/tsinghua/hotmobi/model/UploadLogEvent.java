package edu.tsinghua.hotmobi.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mariotaku on 16/1/2.
 */
@JsonObject
public class UploadLogEvent extends BaseEvent {
    public static final String X_DNEXT_PREFIX = "X-Dnext";
    @JsonField(name = "file_name")
    String fileName;
    @JsonField(name = "file_length")
    long fileLength;
    @JsonField(name = "extra_headers")
    Map<String, String> extraHeaders;

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(Map<String, String> extraHeaders) {
        this.extraHeaders = extraHeaders;
    }

    public void finish(HttpURLConnection response) {
        HashMap<String, String> extraHeaders = new HashMap<>();
        final Map<String, List<String>> headers = response.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            final String name = entry.getKey();
            if (name.regionMatches(true, 0, X_DNEXT_PREFIX, 0, X_DNEXT_PREFIX.length())) {
                for (String value : entry.getValue()) {
                    extraHeaders.put(name, value);
                }
            }
        }
        setExtraHeaders(extraHeaders);
        markEnd();
    }

    public static UploadLogEvent create(Context context, File file) {
        UploadLogEvent event = new UploadLogEvent();
        event.markStart(context);
        event.setFileLength(file.length());
        event.setFileName(file.getName());
        return event;
    }

    @Override
    public String toString() {
        return "UploadLogEvent{" +
                "fileName='" + fileName + '\'' +
                ", fileLength=" + fileLength +
                ", extraHeaders=" + extraHeaders +
                "} " + super.toString();
    }

    public boolean shouldSkip() {
        return fileName.contains(getLogFileName());
    }

    @NonNull
    @Override
    public String getLogFileName() {
        return "upload_log";
    }

}
