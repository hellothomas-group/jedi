package xyz.hellothomas.jedi.admin.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.api.dto.ReleaseHistoryResponse;
import xyz.hellothomas.jedi.admin.application.ReleaseHistoryService;
import xyz.hellothomas.jedi.admin.domain.ReleaseHistory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas
 */
@Api(value = "releaseHistory", tags = "releaseHistory")
@RestController
public class ReleaseHistoryController {

    private Type configurationTypeReference = new TypeToken<Map<String, Object>>() {
    }.getType();

    private final Gson gson;
    private final ReleaseHistoryService releaseHistoryService;

    public ReleaseHistoryController(Gson gson, final ReleaseHistoryService releaseHistoryService) {
        this.gson = gson;
        this.releaseHistoryService = releaseHistoryService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/releases/histories")
    public PageResult<ReleaseHistoryResponse> findReleaseHistoriesByExecutor(
            @PathVariable("namespaceName") String namespaceName,
            @PathVariable("appId") String appId,
            @PathVariable("executorName") String executorName,
            PageHelperRequest pageHelperRequest) {

        PageResult<ReleaseHistory> result = releaseHistoryService.findReleaseHistoriesByNamespace(namespaceName, appId,
                executorName, pageHelperRequest);
        return transform2PageResult(result);
    }


    @GetMapping("/releases/histories/by_release_id_and_operation")
    public PageResult<ReleaseHistoryResponse> findReleaseHistoryByReleaseIdAndOperation(
            @RequestParam("releaseId") long releaseId,
            @RequestParam("operation") int operation,
            PageHelperRequest pageHelperRequest) {

        PageResult<ReleaseHistory> result = releaseHistoryService.findByReleaseIdAndOperation(releaseId, operation,
                pageHelperRequest);

        return transform2PageResult(result);
    }

    @GetMapping("/releases/histories/by_previous_release_id_and_operation")
    public PageResult<ReleaseHistoryResponse> findReleaseHistoryByPreviousReleaseIdAndOperation(
            @RequestParam("previousReleaseId") long previousReleaseId,
            @RequestParam("operation") int operation,
            PageHelperRequest pageHelperRequest) {

        PageResult<ReleaseHistory> result = releaseHistoryService.findByPreviousReleaseIdAndOperation(previousReleaseId,
                operation, pageHelperRequest);

        return transform2PageResult(result);

    }

    private PageResult<ReleaseHistoryResponse> transform2PageResult(PageResult<ReleaseHistory> releaseHistoryPageResult) {
        List<ReleaseHistory> releaseHistories = releaseHistoryPageResult.getContent();
        List<ReleaseHistoryResponse> ReleaseHistoryResponses = new ArrayList<>(releaseHistories.size());
        for (ReleaseHistory releaseHistory : releaseHistories) {
            ReleaseHistoryResponses.add(transformReleaseHistory2DTO(releaseHistory));
        }

        return new PageResult<>(ReleaseHistoryResponses, releaseHistoryPageResult.getTotal(),
                releaseHistoryPageResult.getPageNum(), releaseHistoryPageResult.getPageSize());
    }

    private ReleaseHistoryResponse transformReleaseHistory2DTO(ReleaseHistory releaseHistory) {
        ReleaseHistoryResponse dto = new ReleaseHistoryResponse();
        BeanUtils.copyProperties(releaseHistory, dto, "operationContext");
        dto.setOperationContext(gson.fromJson(releaseHistory.getOperationContext(),
                configurationTypeReference));

        return dto;
    }
}
