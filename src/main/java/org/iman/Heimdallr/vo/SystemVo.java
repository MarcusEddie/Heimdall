/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ey
 *
 */

public class SystemVo implements Serializable {

    private static final long serialVersionUID = -1330707538250336640L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("modules")
    private List<ModuleVo> modules;

    public SystemVo() {
        super();
    }

    public SystemVo(Long id) {
        this.id = id;
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

    public List<ModuleVo> getModules() {
        return modules;
    }

    public void setModules(List<ModuleVo> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "AppStructureVo [id=" + id + ", name=" + name + ", modules=" + modules + "]";
    }

}
