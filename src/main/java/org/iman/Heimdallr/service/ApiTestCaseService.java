/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

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
    
    public ApiTestCase update(ApiTestCaseVo vo) throws DataConversionException;
    
    public Optional<ApiTestCase> delete(ApiTestCaseVo vo) throws DataConversionException;
    
    public Integer delete(List<ApiTestCaseVo> vos) throws DataConversionException;
    
    public Optional<ApiTestCase> activate(ApiTestCaseVo vo) throws DataConversionException;
    
    public Integer activate(List<ApiTestCaseVo> vos) throws DataConversionException;
    
    public Optional<ApiTestCase> deactivate(ApiTestCaseVo vo) throws DataConversionException;
    
    public Integer deactivate(List<ApiTestCaseVo> vos) throws DataConversionException;
}
