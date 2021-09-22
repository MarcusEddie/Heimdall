package org.iman.Heimdallr.vo;

import org.iman.Heimdallr.entity.Page;

public class PageInfo extends BasicPageInfo{

    private static final long serialVersionUID = 9017726998203869198L;

    public Page toPage() {
        Page page = new Page();
        page.setCurrent(getCurrent());
        page.setPageSize(getPageSize());
        return page;
    }

    @Override
    public String toString() {
        return "PageInfo [getPageSize()=" + getPageSize() + ", getCurrent()=" + getCurrent() + "]";
    }
}
