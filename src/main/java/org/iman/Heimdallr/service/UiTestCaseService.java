/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.UiTestCaseVo;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface UiTestCaseService {

    public UiTestCase save(UiTestCaseVo vo) throws DataConversionException;
    
    public Pagination<UiTestCase> getByParams(UiTestCaseVo criteria, Page page) throws DataConversionException;
    
    public Optional<UiTestCase> getById(Long id) throws DataConversionException;
    
    public UiTestCase update(UiTestCaseVo vo) throws DataConversionException;
    
    public Optional<UiTestCase> delete(UiTestCaseVo vo) throws DataConversionException;
    
    public Integer delete(List<UiTestCaseVo> vos) throws DataConversionException;
    
    public Optional<UiTestCase> activate(UiTestCaseVo vo) throws DataConversionException;
    
    public Integer activate(List<UiTestCaseVo> vos) throws DataConversionException;
    
    public Optional<UiTestCase> deactivate(UiTestCaseVo vo) throws DataConversionException;
    
    public Integer deactivate(List<UiTestCaseVo> vos) throws DataConversionException;
}
