package xyz.hellothomas.jedi.admin.api;

import com.google.common.base.Splitter;
import io.swagger.annotations.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.api.dto.ReleaseResponse;
import xyz.hellothomas.jedi.admin.application.ExecutorService;
import xyz.hellothomas.jedi.admin.application.ReleaseService;
import xyz.hellothomas.jedi.admin.application.message.MessageSender;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.common.utils.ReleaseMessageKeyGenerator;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;
import static xyz.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;

/**
 * @author Thomas
 * @date 2021/3/14 23:00
 * @description
 * @version 1.0
 */
@UserLoginToken
@Api(value = "release", tags = "release")
@RestController
public class ReleaseController {
    private static final Splitter RELEASES_SPLITTER = Splitter.on(",").omitEmptyStrings()
            .trimResults();

    private final ReleaseService releaseService;
    private final ExecutorService executorService;
    private final MessageSender messageSender;

    public ReleaseController(ReleaseService releaseService, ExecutorService executorService,
                             MessageSender messageSender) {
        this.releaseService = releaseService;
        this.executorService = executorService;
        this.messageSender = messageSender;
    }

    @GetMapping("/releases/{releaseId}")
    public ApiResponse<ReleaseResponse> get(@PathVariable("releaseId") long releaseId) {
        Release release = releaseService.findOne(releaseId);
        if (release == null) {
            throw new NotFoundException(String.format("release not found for %s", releaseId));
        }
        return ApiResponse.success(LocalBeanUtils.transform(ReleaseResponse.class, release));
    }

    @GetMapping("/releases")
    public ApiResponse<List<ReleaseResponse>> findReleaseByIds(@RequestParam("releaseIds") String releaseIds) {
        Set<Long> releaseIdSet = RELEASES_SPLITTER.splitToList(releaseIds).stream().map(Long::parseLong)
                .collect(Collectors.toSet());

        List<Release> releases = releaseService.findByReleaseIds(releaseIdSet);

        return ApiResponse.success(LocalBeanUtils.batchTransform(ReleaseResponse.class, releases));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/releases/all")
    public ApiResponse<PageResult<ReleaseResponse>> findAllReleases(@PathVariable("namespaceName") String namespaceName,
                                                                    @PathVariable("appId") String appId,
                                                                    @PathVariable("executorName") String executorName,
                                                                    PageHelperRequest pageHelperRequest) {
        PageResult<Release> releasesPage = releaseService.findAllReleases(namespaceName, appId, executorName,
                pageHelperRequest);
        return ApiResponse.success(transform2PageResult(releasesPage));
    }


    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/releases/active")
    public ApiResponse<PageResult<ReleaseResponse>> findActiveReleases(@PathVariable("namespaceName") String namespaceName,
                                                                       @PathVariable("appId") String appId,
                                                                       @PathVariable("executorName") String executorName,
                                                                       PageHelperRequest pageHelperRequest) {
        PageResult<Release> releasesPage = releaseService.findActiveReleases(namespaceName, appId, executorName,
                pageHelperRequest);
        return ApiResponse.success(transform2PageResult(releasesPage));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/releases/latest")
    public ApiResponse<ReleaseResponse> getLatest(@PathVariable("namespaceName") String namespaceName,
                                                  @PathVariable("appId") String appId,
                                                  @PathVariable("executorName") String executorName) {
        Release release = releaseService.findLatestActiveRelease(namespaceName, appId, executorName);
        return ApiResponse.success(LocalBeanUtils.transform(ReleaseResponse.class, release));
    }

    @PreAuthorize(value = "@permissionValidator.hasReleaseExecutorConfigPermission(#namespaceName, #appId, " +
            "#executorName, #operator)")
    @Transactional
    @PostMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/releases")
    public ApiResponse<ReleaseResponse> publish(@PathVariable("namespaceName") String namespaceName,
                                                @PathVariable("appId") String appId,
                                                @PathVariable("executorName") String executorName,
                                                @RequestParam("name") String releaseName,
                                                @RequestParam(name = "comment", required = false) String releaseComment,
                                                @RequestAttribute(CLAIM_USER_NAME) String operator,
                                                @RequestParam(name = "isEmergencyPublish", defaultValue = "false") boolean isEmergencyPublish) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(String.format("Could not find executor for %s %s %s", namespaceName,
                    appId, executor));
        }
        Release release = releaseService.publish(executor, releaseName, releaseComment, operator, isEmergencyPublish);

        //send release message
        messageSender.sendMessage(ReleaseMessageKeyGenerator.generate(namespaceName, appId, executorName),
                JEDI_RELEASE_TOPIC);
        return ApiResponse.success(LocalBeanUtils.transform(ReleaseResponse.class, release));
    }

    @PreAuthorize(value = "@permissionValidator.hasReleaseExecutorConfigPermission(#namespaceName, #appId, " +
            "#executorName, #operator)")
    @Transactional
    @PutMapping("/releases/{releaseId}/rollback")
    public ApiResponse<String> rollback(@PathVariable("releaseId") long releaseId,
                                        @RequestParam(name = "toReleaseId", defaultValue = "-1") long toReleaseId,
                                        @RequestAttribute(CLAIM_USER_NAME) String operator) {

        Release release;
        if (toReleaseId > -1) {
            release = releaseService.rollbackTo(releaseId, toReleaseId, operator);
        } else {
            release = releaseService.rollback(releaseId, operator);
        }

        String namespaceName = release.getNamespaceName();
        String appId = release.getAppId();
        String executorName = release.getExecutorName();
        //send release message
        messageSender.sendMessage(ReleaseMessageKeyGenerator.generate(namespaceName, appId, executorName),
                JEDI_RELEASE_TOPIC);

        return ApiResponse.success("回滚成功");
    }

    private PageResult<ReleaseResponse> transform2PageResult(PageResult<Release> releasePageResult) {
        List<Release> releases = releasePageResult.getContent();
        List<ReleaseResponse> releaseResponses = new ArrayList<>(releases.size());
        for (Release release : releases) {
            releaseResponses.add(LocalBeanUtils.transform(ReleaseResponse.class, release));
        }

        return new PageResult<>(releaseResponses, releasePageResult.getTotal(), releasePageResult.getPageNum(),
                releasePageResult.getPageSize());
    }
}
