package org.asiczen.pettracker.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public interface IOTMessageSource {

	@Output("petMessageQueue")
	MessageChannel iotMessageSource();

	@Output("petAlertMessageQueue")
	MessageChannel petAlertMessageSource();
}
