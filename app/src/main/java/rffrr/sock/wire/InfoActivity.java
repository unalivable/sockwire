package rffrr.sock.wire;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rffrr.sock.wire.core.CoreBridge;

public class InfoActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_info);
        TextView tv = findViewById(R.id.tvInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("SockWire 1.0.0\n\n");
        sb.append("Engine: ").append(CoreBridge.available() ? "loaded" : "missing").append('\n');
        sb.append("Engine version: ").append(CoreBridge.version()).append("\n\n");
        
        sb.append("No Root or ADB needed!\n\n");
        sb.append("SockWire originally created by rFFRr. https://t.me/r_FFR_r\n\n");

        // uhh hey if you're forking my app please don't edit the line above
        // instead please edit those to match your fork
        // thanks

        // - rFFRr

        // sb.append("This app is an forked version!\n");
        // sb.append("This fork is created by [...], and adds these features:\n");
        // sb.append("• feature1\n");
        // sb.append("• feature2\n");
        // sb.append("• feature3\n")

        tv.setText(sb.toString());
    }
}
