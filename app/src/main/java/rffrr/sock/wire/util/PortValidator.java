package rffrr.sock.wire.util;

import java.net.ServerSocket;

public final class PortValidator {
    private PortValidator() {}
    public static boolean isValidRange(int p) { return p >= 1 && p <= 65535; }
    public static boolean isAvailable(int p) {
        if (!isValidRange(p)) return false;
        try (ServerSocket s = new ServerSocket(p)) { return true; }
        catch (Exception e) { return false; }
    }
}
