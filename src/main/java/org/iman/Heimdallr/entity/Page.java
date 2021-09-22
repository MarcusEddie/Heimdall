/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ey
 *
 */
public class Page implements Serializable{

    private static final long serialVersionUID = -7817907985350883563L;

    private Integer pageSize;
    private Integer current;
    private Integer offset;
    private Integer capacity;

    public Page() {
        super();
    }

    public Page(Integer pageSize, Integer current) {
        this.pageSize = pageSize;
        this.capacity = pageSize;
        this.current = current;
    }

    public Integer getOffset() {
        BigDecimal pageNo = new BigDecimal(getCurrent());
        pageNo = pageNo.subtract(new BigDecimal(1));
        pageNo = pageNo.multiply(new BigDecimal(getCapacity()));
        return pageNo.intValue();
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        this.capacity = pageSize;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "Page [offset=" + offset + ", capacity=" + capacity + "]";
    }
}
