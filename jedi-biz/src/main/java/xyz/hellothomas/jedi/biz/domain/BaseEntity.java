package xyz.hellothomas.jedi.biz.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 1: deleted, 0: normal
     */
    protected Boolean isDeleted = false;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    private String updateUser;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    protected ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id)
                .add("createUser", createUser)
                .add("createTime", createTime)
                .add("updateUser", updateUser)
                .add("updateTime", updateTime);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }
}
