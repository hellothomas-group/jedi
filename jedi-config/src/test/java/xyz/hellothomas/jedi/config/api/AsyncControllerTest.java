package xyz.hellothomas.jedi.config.api;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import xyz.hellothomas.jedi.core.utils.SleepUtil;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncControllerTest {
    private static final Splitter EXECUTOR_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    @Test
    public void longPolling() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i <= 9; i++) {
            executorService.execute(() -> {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0" +
                                ".1:8081/async/longPolling",
                        String.class);
                System.err.println("收到响应：" + responseEntity.getBody());
            });
        }
        SleepUtil.sleep(100000);
    }

    @Test
    public void returnLongPollingValue() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity("http://127.0.0.1:8081/async/returnLongPollingValue", null);
    }

    @Test
    public void test() {
        Escaper pathEscaper = UrlEscapers.urlPathSegmentEscaper();
        Escaper queryParamEscaper = UrlEscapers.urlFormParameterEscaper();
        String appId = "LG52.PCC2";
        String cluster = "default";
        String namespace = "dev";

        String path = "configs/%s/%s/%s";
        List<String> pathParams =
                Lists.newArrayList(pathEscaper.escape(appId), pathEscaper.escape(cluster),
                        pathEscaper.escape(namespace));
        String pathExpanded = String.format(path, pathParams.toArray());
        System.out.println(pathExpanded);
    }

    @Test
    public void testGuava() {
        List<String> executorNames = EXECUTOR_SPLITTER.splitToList("d,, f ,e");
    }
}