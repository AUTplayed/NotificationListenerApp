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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import codes.fepi.notificationlistener.dto.NotificationDto;

public class ListenerService2 extends NotificationListenerService {

    private final ObjectMapper mapper;
    private SharedPreferences sharedPref;
    private final ResponseProcessor responseProcessor;

    public ListenerService2() {
        mapper = new ObjectMapper();
        responseProcessor = new ResponseProcessor(this, mapper);
        Log.d("listener", "created");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::periodicUpdate, 1, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.isClearable()) {
            checkForUpdates();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.isClearable()) {
            checkForUpdates();
        }
    }

    private void periodicUpdate() {
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        boolean viableNotif = false;
        for (StatusBarNotification activeNotification : activeNotifications) {
            if(activeNotification.isClearable()) {
                viableNotif = true;
            }
        }
        if(viableNotif) { // only send periodic updates if there are notifications (because no notifs => no actions)
            updateNotifications(activeNotifications);
        }
    }

    private void checkForUpdates() {
        updateNotifications(getActiveNotifications());
    }

    private void updateNotifications(StatusBarNotification[] activeNotifications) {
        Set<NotificationDto> dtos = new HashSet<>(activeNotifications.length);
        for (StatusBarNotification activeNotification : activeNotifications) {
            if (!activeNotification.isClearable() || activeNotification.getId() == 0) {
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
        HttpPostService httpPostService = new HttpPostService(responseProcessor);
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
                sbn.getKey(),
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
