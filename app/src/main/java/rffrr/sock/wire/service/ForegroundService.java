package rffrr.sock.wire.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import rffrr.sock.wire.MainActivity;
import rffrr.sock.wire.R;
import rffrr.sock.wire.core.CoreBridge;

public class ForegroundService extends Service {
    public static final String ACTION_START = "rffrr.sock.wire.START";
    public static final String ACTION_STOP  = "rffrr.sock.wire.STOP";
    public static final String EXTRA_CONF   = "conf";
    private static final int NOTIF_ID = 0x57;

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        if (ACTION_START.equals(intent.getAction())) {
            startForeground(NOTIF_ID, buildNotif());
            String conf = intent.getStringExtra(EXTRA_CONF);
            String err = CoreBridge.start(conf);
            if (err != null && !err.isEmpty()) {
                startActivity(new Intent(this, MainActivity.class)
                        .putExtra("error", err)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                stopSelf();
            }
        } else if (ACTION_STOP.equals(intent.getAction())) {
            CoreBridge.stop();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private Notification buildNotif() {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this, "sockwire")
                .setContentTitle("SockWire")
                .setContentText("SOCKS5 listening")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setOngoing(true)
                .setContentIntent(pi)
                .build();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
    @Override public void onDestroy() { CoreBridge.stop(); super.onDestroy(); }
}
