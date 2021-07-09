package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.NamespaceRequest;
import xyz.hellothomas.jedi.admin.api.dto.NamespaceResponse;
import xyz.hellothomas.jedi.admin.application.NamespaceService;
import xyz.hellothomas.jedi.admin.domain.Namespace;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(value = "namespace", tags = "namespace")
@RestController
public class NamespaceController {

    private final NamespaceService namespaceService;

    public NamespaceController(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    @PostMapping("/namespaces")
    public ApiResponse<NamespaceResponse> create(@Valid @RequestBody NamespaceRequest request,
                                                 @RequestParam String operator) {
        Namespace entity = LocalBeanUtils.transform(Namespace.class, request);
        Namespace managedEntity = namespaceService.findOne(entity.getName());
        if (managedEntity != null) {
            throw new BadRequestException("namespace already exist.");
        }

        entity = namespaceService.save(entity, operator);

        return ApiResponse.success(LocalBeanUtils.transform(NamespaceResponse.class, entity));
    }

    @DeleteMapping("/namespaces/{namespaceName}")
    public ApiResponse<String> delete(@PathVariable("namespaceName") String namespaceName,
                                      @RequestParam String operator) {
        Namespace entity = namespaceService.findOne(namespaceName);
        if (entity == null) {
            throw new NotFoundException("namespace not found for namespaceName " + namespaceName);
        }
        namespaceService.deleteNamespace(entity, operator);

        return ApiResponse.success("删除成功");
    }

    @PutMapping("/namespaces/{namespaceName}")
    public ApiResponse<String> update(@PathVariable String namespaceName, @RequestBody NamespaceRequest request,
                                      @RequestParam String operator) {
        if (!Objects.equals(namespaceName, request.getName())) {
            throw new BadRequestException("The namespace name of path variable and request body is different");
        }

        Namespace entity = LocalBeanUtils.transform(Namespace.class, request);
        namespaceService.update(entity, operator);

        return ApiResponse.success("更新成功");
    }

    @GetMapping("/namespaces")
    public ApiResponse<List<NamespaceResponse>> find(@RequestParam(value = "name", required = false) String name) {
        List<Namespace> app;
        if (StringUtils.isBlank(name)) {
            app = namespaceService.findAll();
        } else {
            app = new ArrayList<>();
            Namespace namespace = namespaceService.findOne(name);
            if (namespace != null) {
                app.add(namespace);
            }
        }
        return ApiResponse.success(LocalBeanUtils.batchTransform(NamespaceResponse.class, app));
    }

    @GetMapping("/namespaces/{namespaceName}")
    public ApiResponse<NamespaceResponse> get(@PathVariable("namespaceName") String namespaceName) {
        Namespace namespace = namespaceService.findOne(namespaceName);
        if (namespace == null) {
            throw new NotFoundException("namespace not found for namespaceName " + namespaceName);
        }
        return ApiResponse.success(LocalBeanUtils.transform(NamespaceResponse.class, namespace));
    }

    @GetMapping("/namespaces/{namespaceName}/unique")
    public ApiResponse<Boolean> isNamespaceUnique(@PathVariable("namespaceName") String namespaceName) {
        return ApiResponse.success(namespaceService.isNamespaceUnique(namespaceName));
    }
}
