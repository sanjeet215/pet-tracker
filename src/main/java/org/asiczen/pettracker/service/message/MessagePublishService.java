package org.asiczen.pettracker.service.message;

import org.asiczen.pettracker.model.message.OriginalMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessagePublishService {
    public void publishMessage(OriginalMessage originalMessage);

    public String resetTemperature(String deviceId);
}
