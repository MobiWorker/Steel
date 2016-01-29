package edu.tsinghua.hotmobi.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mariotaku on 16/1/2.
 */
@JsonObject
public class UploadLogEvent extends BaseEvent {
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

    public void finish(Response response) {
        HashMap<String, String> extraHeaders = new HashMap<>();
        final Headers headers = response.headers();
        for (int i = 0, j = headers.size(); i < j; i++) {
            final String name = headers.name(i);
            if (StringUtils.startsWithIgnoreCase(name, "X-Dnext")) {
                extraHeaders.put(name, headers.value(i));
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
