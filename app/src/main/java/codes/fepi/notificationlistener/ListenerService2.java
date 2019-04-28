package codes.fepi.notificationlistener;

import android.app.Notification;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class ListenerService2 extends NotificationListenerService {

    private final ObjectMapper mapper;
    private SharedPreferences sharedPref;

    public ListenerService2() {
        mapper = new ObjectMapper();
        Log.d("listener", "created");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.isClearable()) {
            updateNotifications();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.isClearable()) {
            updateNotifications();
        }
    }

    private void updateNotifications() {
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        Set<NotificationDto> dtos = new HashSet<>(activeNotifications.length);
        for (StatusBarNotification activeNotification : activeNotifications) {
            if (!activeNotification.isClearable()) {
                continue;
            }
            dtos.add(getDtoFromNotif(activeNotification));
        }
        sendToServer(dtos);
    }

    private void sendToServer(Set<NotificationDto> notifications) {
        String json = "";
        try {
            json = mapper.writeValueAsString(notifications);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpPostService httpPostService = new HttpPostService();
        String baseurl = sharedPref.getString("url", "http://notif.fepi.duckdns.org");
        String secret = sharedPref.getString("secret", "secret");
        String url = baseurl + "?secret=" + secret;
        httpPostService.execute(url, json);
    }

    private NotificationDto getDtoFromNotif(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();

        String title = notification.extras.get(Notification.EXTRA_TITLE).toString();
        Object bigText = notification.extras.get(Notification.EXTRA_BIG_TEXT);
        String content = bigText != null ? bigText.toString() : notification.extras.get(Notification.EXTRA_TEXT).toString();
        String source = getEasyPackageName(sbn.getPackageName());
        NotificationDto dto = new NotificationDto(
                sbn.getId(),
                sbn.getPostTime(),
                title,
                content,
                source
        );
        return dto;
    }

    private String getEasyPackageName(String packageName) {
        String[] packageSplit = packageName.split("\\.");
        if (packageSplit.length > 2) {
            return packageSplit[1] + "." + packageSplit[packageSplit.length - 1];
        }
        return packageName;
    }
}
