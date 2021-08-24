/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constant.Consts;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author ey
 *
 */
@TableName("appStructure")
public class AppStructure extends BaseEntity implements Serializable, Comparable<AppStructure> {

    private static final long serialVersionUID = -1652439544810397724L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer level;

    private Long root;

    public AppStructure() {
        super();
        setCreateBy(Consts.SYSTEM_ADMIN);
        setCreateTime(LocalDateTime.now());
        setEnabled(true);
        setDeleted(false);
    }

    public AppStructure(Long id) {
        this.id = id;
        setCreateBy(Consts.SYSTEM_ADMIN);
        setCreateTime(LocalDateTime.now());
        setEnabled(true);
        setDeleted(false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getRoot() {
        return root;
    }

    public void setRoot(Long root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "AppStructure [id=" + id + ", name=" + name + ", level=" + level + ", root=" + root
                + ", getCreateBy()=" + getCreateBy() + ", getEnabled()=" + getEnabled()
                + ", getDeleted()=" + getDeleted() + ", getCreateTime()=" + getCreateTime()
                + ", getModifiedBy()=" + getModifiedBy() + ", getModifyTime()=" + getModifiedTime()
                + "]";
    }

    @Override
    public int compareTo(AppStructure o) {
        return this.getName().compareTo(o.getName());
    }
}
