package edu.tsinghua.location.research;

import android.app.IntentService;
import android.content.Intent;

import edu.tsinghua.hotmobi.UploadLogsTask;

/**
 * Created by mariotaku on 16/2/10.
 */
public class UploadLogsService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadLogsService() {
        super("steel_log_uploader");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final UploadLogsTask task = new UploadLogsTask(this);
        task.run();
    }
}
