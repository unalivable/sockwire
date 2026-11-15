package rffrr.sock.wire;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import rffrr.sock.wire.util.ProfileStore;

public class ProfileEditActivity extends AppCompatActivity {
    private int idx;
    private Profile p;
    private EditText edName, edPrivate, edPublic, edPreshared, edAddress, edEndpoint, edMtu, edKeepalive;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_edit);
        idx = getIntent().getIntExtra("idx", -1);
        p = (idx >= 0) ? ProfileStore.get(idx) : new Profile();
        if (p == null) { finish(); return; }

        edName = findViewById(R.id.edName);
        edPrivate = findViewById(R.id.edPrivate);
        edPublic = findViewById(R.id.edPublic);
        edPreshared = findViewById(R.id.edPreshared);
        edAddress = findViewById(R.id.edAddress);
        edEndpoint = findViewById(R.id.edEndpoint);
        edMtu = findViewById(R.id.edMtu);
        edKeepalive = findViewById(R.id.edKeepalive);

        edName.setText(p.name);
        edPrivate.setText(p.privateKey);
        edPublic.setText(p.publicKey);
        edPreshared.setText(p.presharedKey);
        edAddress.setText(p.address);
        edEndpoint.setText(p.endpoint);
        edMtu.setText(String.valueOf(p.mtu));
        edKeepalive.setText(String.valueOf(p.keepalive));

        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnConnect = findViewById(R.id.btnConnect);

        if (idx < 0) btnDelete.setEnabled(false);

        btnSave.setOnClickListener(v -> { collect(); save(); finish(); });
        btnDelete.setOnClickListener(v -> { if (idx >= 0) ProfileStore.remove(idx); finish(); });
        btnConnect.setOnClickListener(v -> {
            collect();
            if (!p.isValid()) { btnConnect.setError("incomplete"); return; }
            save();
            MainActivity m = null;
            // Simple: go back to MainActivity and connect
            finish();
        });
    }

    private void collect() {
        p.name = edName.getText().toString().trim();
        p.privateKey = edPrivate.getText().toString().trim();
        p.publicKey = edPublic.getText().toString().trim();
        p.presharedKey = edPreshared.getText().toString().trim();
        p.address = edAddress.getText().toString().trim();
        p.endpoint = edEndpoint.getText().toString().trim();
        try { p.mtu = Integer.parseInt(edMtu.getText().toString().trim()); } catch (Exception e) { p.mtu = 1280; }
        try { p.keepalive = Integer.parseInt(edKeepalive.getText().toString().trim()); } catch (Exception e) { p.keepalive = 5; }
    }

    private void save() {
        if (idx < 0) ProfileStore.add(p);
        else ProfileStore.update(idx, p);
    }
}
