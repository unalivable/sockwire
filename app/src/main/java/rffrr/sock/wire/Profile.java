package rffrr.sock.wire;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {
    public String name = "";
    public String privateKey = "";
    public String publicKey = "";
    public String presharedKey = "";
    public String address = "";
    public String endpoint = "";
    public int mtu = 1280;
    public int keepalive = 5;

    public String display() {
        if (name != null && !name.isEmpty()) return name + " — " + endpoint;
        return endpoint;
    }

    /** Generate wireproxy .conf text for this profile + SOCKS5 port. */
    public String toConf(int socksPort) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Interface]\n");
        if (!address.isEmpty()) sb.append("Address = ").append(address).append('\n');
        if (!privateKey.isEmpty()) sb.append("PrivateKey = ").append(privateKey).append('\n');
        sb.append("DNS = 77.88.8.8\n");
        sb.append("MTU = ").append(mtu).append('\n');
        sb.append('\n');
        sb.append("[Peer]\n");
        if (!publicKey.isEmpty()) sb.append("PublicKey = ").append(publicKey).append('\n');
        if (!presharedKey.isEmpty()) sb.append("PresharedKey = ").append(presharedKey).append('\n');
        sb.append("Endpoint = ").append(endpoint).append('\n');
        sb.append("PersistentKeepalive = ").append(keepalive).append('\n');
        sb.append("AllowedIPs = 0.0.0.0/0\n");
        sb.append('\n');
        sb.append("[Socks5]\n");
        sb.append("BindAddress = 127.0.0.1:").append(socksPort).append('\n');
        return sb.toString();
    }

    public boolean isValid() {
        return !privateKey.isEmpty() && !publicKey.isEmpty()
            && !endpoint.isEmpty() && endpoint.contains(":");
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("name", name);
        o.put("privateKey", privateKey);
        o.put("publicKey", publicKey);
        o.put("presharedKey", presharedKey);
        o.put("address", address);
        o.put("endpoint", endpoint);
        o.put("mtu", mtu);
        o.put("keepalive", keepalive);
        return o;
    }

    public static Profile fromJson(JSONObject o) {
        Profile p = new Profile();
        p.name = o.optString("name", "");
        p.privateKey = o.optString("privateKey", "");
        p.publicKey = o.optString("publicKey", "");
        p.presharedKey = o.optString("presharedKey", "");
        p.address = o.optString("address", "");
        p.endpoint = o.optString("endpoint", "");
        p.mtu = o.optInt("mtu", 1280);
        p.keepalive = o.optInt("keepalive", 5);
        return p;
    }
}
