package rffrr.sock.wire;

import android.app.Application;
import rffrr.sock.wire.util.ProfileStore;

public class SockWireApp extends Application {
    @Override public void onCreate() {
        super.onCreate();
        ProfileStore.init(this);
    }
}
