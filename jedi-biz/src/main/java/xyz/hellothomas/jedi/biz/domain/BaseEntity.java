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
     * 创建人邮箱前缀
     */
    private String dataChangeCreatedBy;

    /**
     * 创建时间
     */
    private LocalDateTime dataChangeCreatedTime;

    /**
     * 最后修改人邮箱前缀
     */
    private String dataChangeLastModifiedBy;

    /**
     * 最后修改时间
     */
    private LocalDateTime dataChangeLastModifiedTime;

    protected ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id)
                .add("dataChangeCreatedBy", dataChangeCreatedBy)
                .add("dataChangeCreatedTime", dataChangeCreatedTime)
                .add("dataChangeLastModifiedBy", dataChangeLastModifiedBy)
                .add("dataChangeLastModifiedTime", dataChangeLastModifiedTime);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }
}
