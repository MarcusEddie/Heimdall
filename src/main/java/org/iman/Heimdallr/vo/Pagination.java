/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author ey
 * @param <T>
 *
 */
public class Pagination<T> implements Serializable{

    private static final long serialVersionUID = 8971766987842331906L;

    private List<T> list;
    
    private Integer current;
    
    private Integer pageSize;
    
    private Integer total;

    public Pagination() {
        super();
    }

    public Pagination(List<T> list) {
        this.list = list;
    }

    public Pagination(Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
    }

    public Pagination(List<T> list, Integer current, Integer pageSize, Integer total) {
        this.list = list;
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pagination [list=" + list + ", current=" + current + ", pageSize=" + pageSize
                + ", total=" + total + "]";
    }
}
