package rffrr.sock.wire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import rffrr.sock.wire.core.CoreBridge;
import rffrr.sock.wire.service.ForegroundService;
import rffrr.sock.wire.util.PortValidator;
import rffrr.sock.wire.util.ProfileStore;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ListView list;
    private TextView status;
    private EditText portInput;
    private Button btnDisconnect;
    private SharedPreferences prefs;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("sockwire", MODE_PRIVATE);
        status = findViewById(R.id.status);
        portInput = findViewById(R.id.portInput);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        list = findViewById(R.id.list);

        portInput.setText(String.valueOf(prefs.getInt("port", 5599)));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, v, pos, id) -> {
            Intent i = new Intent(this, ProfileEditActivity.class);
            i.putExtra("idx", pos);
            startActivity(i);
        });

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent i = new Intent(this, ProfileEditActivity.class);
            i.putExtra("idx", -1);
            startActivity(i);
        });
        findViewById(R.id.btnInfo).setOnClickListener(v ->
                startActivity(new Intent(this, InfoActivity.class)));
        btnDisconnect.setOnClickListener(v -> disconnect());

        if (!CoreBridge.available()) status.setText(R.string.engine_missing);
        String err = getIntent().getStringExtra("error");
        if (err != null) Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        adapter.clear();
        if (ProfileStore.size() == 0) {
            status.setText(R.string.no_profiles);
        } else {
            for (Profile p : ProfileStore.all()) adapter.add(p.display());
            if (!CoreBridge.isRunning()) status.setText(R.string.status_idle);
        }
        adapter.notifyDataSetChanged();
    }

    public void connectProfile(Profile p) {
        int port = safePort();
        if (!PortValidator.isAvailable(port)) {
            Toast.makeText(this, R.string.port_busy, Toast.LENGTH_LONG).show();
            return;
        }
        if (!p.isValid()) {
            Toast.makeText(this, "profile incomplete", Toast.LENGTH_LONG).show();
            return;
        }
        Intent svc = new Intent(this, ForegroundService.class);
        svc.setAction(ForegroundService.ACTION_START);
        svc.putExtra(ForegroundService.EXTRA_CONF, p.toConf(port));
        startService(svc);
        status.setText(getString(R.string.status_running) + ":" + port);
        btnDisconnect.setVisibility(Button.VISIBLE);
    }

    private void disconnect() {
        Intent svc = new Intent(this, ForegroundService.class);
        svc.setAction(ForegroundService.ACTION_STOP);
        startService(svc);
        status.setText(R.string.status_idle);
        btnDisconnect.setVisibility(Button.GONE);
    }

    private int safePort() {
        int p;
        try { p = Integer.parseInt(portInput.getText().toString().trim()); }
        catch (Exception e) { p = 5599; }
        prefs.edit().putInt("port", p).apply();
        return p;
    }
}
