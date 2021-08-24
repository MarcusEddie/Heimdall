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
public class App implements Serializable{

    private static final long serialVersionUID = -4999070610134732540L;

    private Long id;

    private String name;

    private List<Module> modules;

    public App() {
        super();
    }

    public App(Long id) {
        this.id = id;
    }

    public App(Long id, String name) {
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

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "app [id=" + id + ", name=" + name + ", modules=" + modules + "]";
    }
}
