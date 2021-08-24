/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

/**
 * @author ey
 *
 */
public class Function implements Serializable{

    private static final long serialVersionUID = -6557301169617104307L;

    private Long id;

    private String name;

    private Long root;

    public Function() {
        super();
    }

    public Function(Long id) {
        this.id = id;
    }

    public Function(Long id, String name) {
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

    @Override
    public String toString() {
        return "Function [id=" + id + ", name=" + name + ", root=" + root + "]";
    }
    
}
