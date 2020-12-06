package com.cat.origin.kafka;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/kafka")
public class KafkaController {


    @Autowired(required = false)
    private KafkaProducer kafkaProducer;

    @GetMapping("/send")
    void kafka() {
//        Map map = Maps.newHashMap();
//        map.put("test", "send message");
        kafkaProducer.send("send message");
    }
}
