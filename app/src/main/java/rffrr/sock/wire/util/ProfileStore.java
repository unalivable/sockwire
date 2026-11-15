package rffrr.sock.wire.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import rffrr.sock.wire.Profile;

public final class ProfileStore {
    private static final String PREF = "sockwire";
    private static final String KEY = "profiles";
    private static SharedPreferences sp;
    private static final List<Profile> profiles = new ArrayList<>();

    private ProfileStore() {}

    public static void init(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        load();
    }

    private static void load() {
        profiles.clear();
        String raw = sp.getString(KEY, "[]");
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                profiles.add(Profile.fromJson(arr.getJSONObject(i)));
            }
        } catch (Exception ignored) {}
    }

    public static void save() {
        JSONArray arr = new JSONArray();
        try {
            for (Profile p : profiles) arr.put(p.toJson());
        } catch (Exception ignored) {}
        sp.edit().putString(KEY, arr.toString()).apply();
    }

    public static List<Profile> all() { return profiles; }
    public static Profile get(int i) { return (i >= 0 && i < profiles.size()) ? profiles.get(i) : null; }
    public static int size() { return profiles.size(); }
    public static int add(Profile p) { profiles.add(p); save(); return profiles.size() - 1; }
    public static void update(int i, Profile p) { if (i >= 0 && i < profiles.size()) { profiles.set(i, p); save(); } }
    public static void remove(int i) { if (i >= 0 && i < profiles.size()) { profiles.remove(i); save(); } }
}
