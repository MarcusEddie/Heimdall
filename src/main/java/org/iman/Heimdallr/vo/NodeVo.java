package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.util.List;

import org.iman.Heimdallr.constants.enums.NodeDirection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeVo implements Serializable {

    private static final long serialVersionUID = 3442670342592636732L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("children")
    private List<NodeVo> children;

    @JsonProperty("side")
    private NodeDirection side;

    public NodeVo() {
        super();
    }

    public NodeVo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<NodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<NodeVo> children) {
        this.children = children;
    }

    public NodeDirection getSide() {
        return side;
    }

    public void setSide(NodeDirection side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "NodeVo [id=" + id + ", label=" + label + ", children=" + children + ", side=" + side
                + "]";
    }
}
