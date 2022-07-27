package org.asiczen.pettracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.asiczen.pettracker.model.message.OriginalMessage;
import org.asiczen.pettracker.service.message.MessagePublishService;
import org.asiczen.pettracker.source.IOTMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet")
@Slf4j
public class MessageController {

    @Autowired
    MessagePublishService messagePublishService;

    @PostMapping("/message")
    public ResponseEntity<?> publishMessage(@RequestBody OriginalMessage message) {
        messagePublishService.publishMessage(message);
        return new ResponseEntity<>("message published successfully", HttpStatus.CREATED);
    }
}
