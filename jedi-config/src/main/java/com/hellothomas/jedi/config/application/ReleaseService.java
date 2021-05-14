package com.hellothomas.jedi.config.application;

import com.hellothomas.jedi.biz.domain.Release;
import com.hellothomas.jedi.biz.domain.ReleaseExample;
import com.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseService {

    private final ReleaseMapper releaseMapper;

    public ReleaseService(ReleaseMapper releaseMapper) {
        this.releaseMapper = releaseMapper;
    }

    public Release findActiveOne(long releaseId) {
        Release release = releaseMapper.selectByPrimaryKey(releaseId);
        if (release != null && release.getIsAbandoned()) {
            return null;
        }

        return release;
    }

    public Release findLatestActiveRelease(String namespaceName, String appId, String executorName) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsAbandonedEqualTo(false);
        releaseExample.setOrderByClause("id desc limit 1");
        List<Release> result = releaseMapper.selectByExampleWithBLOBs(releaseExample);
        return result.isEmpty() ? null : result.get(0);
    }
}
