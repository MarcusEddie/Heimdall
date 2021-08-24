/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ey
 *
 */
public class FunctionVo implements Serializable {

    private static final long serialVersionUID = 4176012131087066867L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("root")
    private Long root;

    public FunctionVo() {
        super();
    }

    public FunctionVo(Long id) {
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

    @Override
    public String toString() {
        return "Function [id=" + id + ", name=" + name + ", root=" + root + "]";
    }

}
