/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ey
 *
 */
public class BasicPageInfo implements Serializable{

    private static final long serialVersionUID = 2383793491265876007L;

    @Positive
    @JsonProperty("pageSize")
    private Integer pageSize = 20;

    @Positive
    @JsonProperty("current")
    private Integer current = 1;

    public BasicPageInfo() {
        super();
    }

    public BasicPageInfo(@Positive Integer pageSize, @Positive Integer current) {
        this.pageSize = pageSize;
        this.current = current;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "BasicPageInfo [pageSize=" + pageSize + ", current=" + current + "]";
    }
}