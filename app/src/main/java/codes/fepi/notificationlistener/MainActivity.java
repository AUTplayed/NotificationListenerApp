package codes.fepi.notificationlistener;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private EditText url, secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        url = findViewById(R.id.url);
        url.setText(sharedPref.getString("url", "http://notif.fepi.duckdns.org"));
        secret = findViewById(R.id.secret);
        secret.setText(sharedPref.getString("secret", "secret"));
        button.setOnTouchListener(this::createNotification);
    }

    private boolean createNotification(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            sharedPref.edit()
                    .putString("url", url.getText().toString())
                    .putString("secret", secret.getText().toString())
                    .commit();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test")
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("title")
                    .setContentText("content")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify((int)(Math.random() * 1000), builder.build());
            return true;
        }
        return false;
    }
}
