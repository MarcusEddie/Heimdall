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
public class ModuleVo implements Serializable {

    private static final long serialVersionUID = -1396064230988462549L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("root")
    private Long root;

    @JsonProperty("functions")
    private List<FunctionVo> functions;

    public ModuleVo() {
        super();
    }

    public ModuleVo(Long id) {
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

    public Long getRoot() {
        return root;
    }

    public void setRoot(Long root) {
        this.root = root;
    }

    public List<FunctionVo> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionVo> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "ModuleVo [id=" + id + ", name=" + name + ", root=" + root + ", functions="
                + functions + "]";
    }

}
