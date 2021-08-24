/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ey
 *
 */
public class Module implements Serializable{

    private static final long serialVersionUID = -1278191096945359438L;

    private Long id;

    private String name;

    private Long root;

    private List<Function> functions;

    public Module() {
        super();
    }

    public Module(Long id) {
        this.id = id;
    }

    public Module(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "Module [id=" + id + ", name=" + name + ", root=" + root + ", functions=" + functions
                + "]";
    }
    
}
