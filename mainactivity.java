package com.malik.finder;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    EditText nameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput = findViewById(R.id.wakeWord);
        Button start = findViewById(R.id.startBtn);
        Button stop = findViewById(R.id.stopBtn);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1);

        start.setOnClickListener(v -> {
            String wake = nameInput.getText().toString();
            Intent i = new Intent(this, MalikService.class);
            i.putExtra("WAKE", wake);
            startForegroundService(i);
        });
        stop.setOnClickListener(v -> stopService(new Intent(this, MalikService.class)));
    }
}