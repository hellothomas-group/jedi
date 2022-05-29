package xyz.hellothomas.jedi.collector.application;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public App findOne(String namespaceName, String appId) {
        Preconditions
                .checkArgument(!StringUtils.isAnyBlank(namespaceName, appId), "Namespace or appId must not" +
                        " be null");
        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);

        List<App> apps = appMapper.selectByExample(appExample);

        return apps.isEmpty() ? null : apps.get(0);
    }

    public void save(App app) {
        try {
            appMapper.insertSelective(app);
        } catch (Exception e) {
            appMapper.updateByPrimaryKey(app);
        }
    }
}
