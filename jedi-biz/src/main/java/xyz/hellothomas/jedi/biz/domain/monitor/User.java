package xyz.hellothomas.jedi.biz.domain.monitor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 10:41
 * @descripton
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class User {
    /**
     * 自增Id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户真实名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 0: 系统生成, 1: 手工生成
     */
    private Boolean isManual;

    /**
     * 1: 有效, 0: 无效
     */
    private Boolean enabled;
}
