/**
 * 
 */
package org.iman.Heimdallr.service;

import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.ApiTestCaseVo;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface ApiTestCaseService {

    public ApiTestCase save(ApiTestCaseVo vo) throws DataConversionException;
    
    public Pagination<ApiTestCase> getByParams(ApiTestCaseVo criteria, Page page) throws DataConversionException;
    
}
