package codes.fepi.notificationlistener;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import codes.fepi.notificationlistener.dto.ActionDto;

public class ResponseProcessor {

    private final ListenerService2 listenerService;
    private final ObjectMapper mapper;

    public ResponseProcessor(ListenerService2 listenerService, ObjectMapper mapper) {
        this.listenerService = listenerService;
        this.mapper = mapper;
    }

    public void process(String response) {
        try {
            ActionDto action = mapper.readValue(response, ActionDto.class);
            if (!action.hasActions()) {
                return;
            }
            if (action.getClear().size() > 0) {
                processClear(action.getClear());
            }
        } catch (Exception e) {
            Log.d("responseProcessor", "Response was not an ActionDto:\n" + response);
        }
    }

    private void processClear(Set<String> clear) {
        listenerService.cancelNotifications(clear.toArray(new String[0]));
    }
}
