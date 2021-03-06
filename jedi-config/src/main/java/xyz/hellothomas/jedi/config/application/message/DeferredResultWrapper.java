package xyz.hellothomas.jedi.config.application.message;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.async.DeferredResult;
import xyz.hellothomas.jedi.core.dto.config.JediExecutorConfigNotification;

import java.util.List;
import java.util.Map;

public class DeferredResultWrapper implements Comparable<DeferredResultWrapper> {
    private static final ResponseEntity<List<JediExecutorConfigNotification>>
            NOT_MODIFIED_RESPONSE_LIST = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

    private Map<String, String> normalizedExecutorNameToOriginalExecutorName;
    private DeferredResult<ResponseEntity<List<JediExecutorConfigNotification>>> result;


    public DeferredResultWrapper(long timeoutInMilli) {
        result = new DeferredResult<>(timeoutInMilli, NOT_MODIFIED_RESPONSE_LIST);
    }

    public void recordNamespaceNameNormalizedResult(String originalNamespaceName, String normalizedNamespaceName) {
        if (normalizedExecutorNameToOriginalExecutorName == null) {
            normalizedExecutorNameToOriginalExecutorName = Maps.newHashMap();
        }
        normalizedExecutorNameToOriginalExecutorName.put(normalizedNamespaceName, originalNamespaceName);
    }

    public void onTimeout(Runnable timeoutCallback) {
        result.onTimeout(timeoutCallback);
    }

    public void onCompletion(Runnable completionCallback) {
        result.onCompletion(completionCallback);
    }


    public void setResult(JediExecutorConfigNotification notification) {
        setResult(Lists.newArrayList(notification));
    }

    public void setResult(List<JediExecutorConfigNotification> notifications) {
        result.setResult(new ResponseEntity<>(notifications, HttpStatus.OK));
    }

    public DeferredResult<ResponseEntity<List<JediExecutorConfigNotification>>> getResult() {
        return result;
    }

    @Override
    public int compareTo(@NonNull DeferredResultWrapper deferredResultWrapper) {
        return Integer.compare(this.hashCode(), deferredResultWrapper.hashCode());
    }
}
