package xyz.hellothomas.jedi.admin.api;

import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.AppRequest;
import xyz.hellothomas.jedi.admin.api.dto.AppResponse;
import xyz.hellothomas.jedi.admin.application.AppService;
import xyz.hellothomas.jedi.admin.domain.App;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author 80234613
 */
@RestController
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/namespaces/{namespaceName}/apps/{appId}")
    public AppResponse create(@PathVariable("namespaceName") String namespaceName,
                              @Valid @RequestBody AppRequest appRequest, @RequestParam String operator) {

        App entity = LocalBeanUtils.transform(App.class, appRequest);
        App managedEntity = appService.findOne(entity.getNamespaceName(), entity.getAppId());

        if (managedEntity != null) {
            throw new BadRequestException("app namespaces already exist.");
        }

        entity = appService.save(entity, operator);

        return LocalBeanUtils.transform(AppResponse.class, entity);
    }

    @PutMapping("/namespaces/{namespaceName}/apps/{appId}")
    public void update(@PathVariable String namespaceName, @RequestBody AppRequest request,
                       @RequestParam String operator) {
        if (!Objects.equals(namespaceName, request.getNamespaceName())) {
            throw new BadRequestException("The namespace name of path variable and request body is different");
        }

        App entity = LocalBeanUtils.transform(App.class, request);

        appService.update(entity, operator);
    }

    @DeleteMapping("/namespaces/{namespaceName}/apps/{appId}")
    public void delete(@PathVariable("namespaceName") String namespaceName, @PathVariable("appId") String appId,
                       @RequestParam String operator) {
        App entity = appService.findOne(namespaceName, appId);
        if (entity == null) {
            throw new BadRequestException("namespace app not found for namespace: " + namespaceName + " " +
                    "appId: " + appId);
        }
        appService.deleteApp(entity, operator);
    }

    @GetMapping("/namespaces/{namespaceName}/apps")
    public List<AppResponse> getAppNamespaces(@PathVariable("namespaceName") String namespaceName) {
        List<App> appNamespaces = appService.findByNamespaceName(namespaceName);

        return LocalBeanUtils.batchTransform(AppResponse.class, appNamespaces);
    }
}
