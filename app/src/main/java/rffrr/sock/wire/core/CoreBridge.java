package rffrr.sock.wire.core;

import android.util.Log;
import java.lang.reflect.Method;

public final class CoreBridge {
    private static final String TAG = "SockWire/Core";
    private static final String CLS = "rffrr.sock.wire.core.Mobile";

    private static final boolean AVAILABLE;
    private static final Method M_START, M_STOP, M_RUNNING, M_VERSION;

    static {
        Method s = null, t = null, r = null, v = null;
        boolean ok = false;
        try {
            Class<?> c = Class.forName(CLS);
            s = c.getMethod("start", String.class);
            t = c.getMethod("stop");
            r = c.getMethod("isRunning");
            v = c.getMethod("version");
            ok = true;
        } catch (Throwable e) {
            Log.w(TAG, "engine class not present: " + e.getMessage());
        }
        AVAILABLE = ok;
        M_START = s; M_STOP = t; M_RUNNING = r; M_VERSION = v;
    }

    private CoreBridge() {}
    public static boolean available() { return AVAILABLE; }
    public static String start(String conf) {
        if (!AVAILABLE) return "engine-not-loaded";
        try { return (String) M_START.invoke(null, conf); }
        catch (Throwable e) { return "start:" + e.getMessage(); }
    }
    public static String stop() {
        if (!AVAILABLE) return "";
        try { return (String) M_STOP.invoke(null); }
        catch (Throwable e) { return "stop:" + e.getMessage(); }
    }
    public static boolean isRunning() {
        if (!AVAILABLE) return false;
        try { return (Boolean) M_RUNNING.invoke(null); }
        catch (Throwable e) { return false; }
    }
    public static String version() {
        if (!AVAILABLE) return "n/a";
        try { return (String) M_VERSION.invoke(null); }
        catch (Throwable e) { return "err"; }
    }
}
