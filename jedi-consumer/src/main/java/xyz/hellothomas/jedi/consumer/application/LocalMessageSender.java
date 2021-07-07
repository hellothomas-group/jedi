package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 80234613 唐圆
 * @date 2021/7/7 9:13
 * @descripton
 * @version 1.0
 */
@Slf4j
@Service
public class LocalMessageSender implements MessageSender {

    @Override
    public void notify(List<String> users, String msg) {
        log.warn("notify {} : {}", String.join(",", users), msg);
    }
}
