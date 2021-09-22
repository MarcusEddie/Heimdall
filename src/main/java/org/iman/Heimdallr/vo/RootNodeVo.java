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
public class RootNodeVo implements Serializable {

    private static final long serialVersionUID = -6717192746269774077L;

    @JsonProperty("roots")
    private List<NodeVo> roots;

    public RootNodeVo() {
        super();
    }

    public RootNodeVo(List<NodeVo> roots) {
        this.roots = roots;
    }

    public List<NodeVo> getRoots() {
        return roots;
    }

    public void setRoots(List<NodeVo> roots) {
        this.roots = roots;
    }

    @Override
    public String toString() {
        return "RootNodeVo [roots=" + roots + "]";
    }

}