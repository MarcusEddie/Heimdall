/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;

import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.entity.DataHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface DataHistoryService {

    public DataHistory save(Object curObj, Object newObj, Long dataId, Action action, FuncTag tag, String opr) throws DataConversionException;
    
    public Pagination<DataHistory> getLogsByDataIdAndFuncTag(Long id, FuncTag tag, Page page)
            throws DataConversionException;
    
    public List<DataHistory> getLogsByDataIdAndFuncTag(Long id, FuncTag tag)
            throws DataConversionException;

}
