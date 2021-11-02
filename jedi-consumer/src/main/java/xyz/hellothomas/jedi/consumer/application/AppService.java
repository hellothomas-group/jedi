package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.biz.domain.monitor.AppExample;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.monitor.AppMapper;

import java.util.List;

@Slf4j
@Service
public class AppService {

    private final AppMapper appMapper;

    public AppService(AppMapper appMapper) {
        this.appMapper = appMapper;
    }

    public List<App> findAppIds() {
        AppExample appExample = new AppExample();
        appExample.createCriteria().andIsDeletedEqualTo(false);

        return appMapper.selectByExample(appExample);
    }
}
