package codes.fepi.notificationlistener;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codes.fepi.notificationlistener.dto.NotificationDto;

import static org.junit.Assert.assertEquals;

public class ListenerServiceTest {

    ListenerService2 listener = new ListenerService2();

    @Test
    public void testFilter() {
        Set<NotificationDto> notifs = new HashSet<>();
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "abc")); //duplicate
        List<NotificationDto> result = listener.filterNotifications(notifs);
        assertEquals(1, result.size());

        notifs = new HashSet<>();
        notifs.add(new NotificationDto(1, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(2, null, 0, "abc", "abc", "abc")); //duplicate
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "abc")); // duplicate
        result = listener.filterNotifications(notifs);
        assertEquals(1, result.size());

        notifs = new HashSet<>();
        notifs.add(new NotificationDto(1, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(2, null, 0, "abcd", "abcd", "abc"));
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "abc")); // duplicate
        result = listener.filterNotifications(notifs);
        assertEquals(2, result.size());

        notifs = new HashSet<>();
        notifs.add(new NotificationDto(1, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(2, null, 0, "abcd", "", "abc")); //duplicate
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "ddddd"));
        result = listener.filterNotifications(notifs);
        assertEquals(2, result.size());

        notifs = new HashSet<>();
        notifs.add(new NotificationDto(1, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(2, null, 0, "abcd", "", "abc")); //duplicate
        notifs.add(new NotificationDto(3, null, 0, "abc", "abc", "ddddd"));
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "ddddd")); //duplicate
        result = listener.filterNotifications(notifs);
        assertEquals(2, result.size());

        notifs = new HashSet<>();
        notifs.add(new NotificationDto(1, null, 0, "abc", "", "abc")); //duplicate
        notifs.add(new NotificationDto(2, null, 0, "abc", "abc", "abc"));
        notifs.add(new NotificationDto(0, null, 0, "abc", "abc", "abc")); // duplicate
        result = listener.filterNotifications(notifs);
        assertEquals(1, result.size());
    }

}