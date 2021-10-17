package xyz.hellothomas.jedi.config.application;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.hellothomas.jedi.biz.domain.config.ReleaseMessage;
import xyz.hellothomas.jedi.biz.domain.config.ReleaseMessageExample;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.ReleaseMessageMapper;

import java.util.List;

@Service
public class ReleaseMessageService {
    private final ReleaseMessageMapper releaseMessageMapper;

    public ReleaseMessageService(final ReleaseMessageMapper releaseMessageMapper) {
        this.releaseMessageMapper = releaseMessageMapper;
    }

    public ReleaseMessage findLatestReleaseMessageForMessages(List<String> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return null;
        }

        ReleaseMessageExample releaseMessageExample = new ReleaseMessageExample();
        releaseMessageExample.createCriteria().andMessageIn(messages);
        releaseMessageExample.setOrderByClause("id desc");

        List<ReleaseMessage> releaseMessages = releaseMessageMapper.selectByExample(releaseMessageExample);

        return releaseMessages.isEmpty() ? null : releaseMessages.get(0);
    }
}
