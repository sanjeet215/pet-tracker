package org.asiczen.pettracker.publisher;

import org.asiczen.pettracker.source.IOTMessageSource;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(IOTMessageSource.class)
public class IOTMessagePublisher {

}
