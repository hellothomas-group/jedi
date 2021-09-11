package xyz.hellothomas.jedi.admin.infrastructure.inteceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.hellothomas.jedi.admin.application.UserService;
import xyz.hellothomas.jedi.admin.common.enums.AdminErrorCodeEnum;
import xyz.hellothomas.jedi.admin.common.utils.JwtUtil;
import xyz.hellothomas.jedi.admin.domain.User;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PassToken;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 10:55
 * @descripton
 * @version 1.0
 */
@Slf4j
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public JwtAuthenticationInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object object) throws Exception {
        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
        String token = httpServletRequest.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查方法是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查方法有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (!userLoginToken.required()) {
                return true;
            }

            jwtVerify(httpServletRequest, token);

            return true;
        }

        Class<?> clazz = handlerMethod.getBeanType();
        //检查类是否有passtoken注释，有则跳过认证
        if (clazz.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = clazz.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查类有没有需要用户权限的注解
        if (clazz.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = clazz.getAnnotation(UserLoginToken.class);
            if (!userLoginToken.required()) {
                return true;
            }

            jwtVerify(httpServletRequest, token);

            return true;
        }

        return true;
    }

    /**
     * 校验token并解析载荷内容供下游使用
     *
     * @param httpServletRequest
     * @param token
     */
    private void jwtVerify(HttpServletRequest httpServletRequest, String token) {
        log.debug("被jwt拦截需要验证");
        // 执行认证
        if (token == null) {
            //这里其实是登录失效,没token了   这个错误也是我自定义的，读者需要自己修改
            throw new BusinessException(AdminErrorCodeEnum.TOKEN_IS_NULL);
        }

        // 获取 token 中的 userId
        String userId = JwtUtil.getAudience(token);

        //找找看是否有这个user   因为我们需要检查用户是否存在，读者可以自行修改逻辑
        User user = userService.getUserById(Integer.parseInt(userId));

        if (user == null) {
            //这个错误也是我自定义的
            throw new BusinessException(AdminErrorCodeEnum.USER_NOT_EXIST);
        }

        // 验证 token
        JwtUtil.verifyToken(token, user.getPassword());

        //获取载荷内容
        String userName = JwtUtil.getClaimByName(token, CLAIM_USER_NAME).asString();

        //放入attribute以便后面调用
        httpServletRequest.setAttribute(CLAIM_USER_NAME, userName);
    }
}
