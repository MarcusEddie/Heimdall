/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.exception.DataNotFoundException;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TestCaseVo;

/**
 * @author ey
 *
 */
public interface CaseGeneralInfoService {

    public Optional<TestCase> saveOneCase(TestCaseVo vo) throws DataNotFoundException;

    public Integer saveMultiCases(List<TestCaseVo> cases) throws DataConversionException;

    public List<TestCase> getByParams(TestCaseVo vo) throws DataConversionException;
    
    public Pagination<TestCase> getByParams(TestCaseVo vo, Page page) throws DataConversionException;

    public Optional<TestCase> updateTestCase(TestCaseVo vo) throws DataConversionException;

    public Optional<TestCase> deactivate(TestCaseVo vo) throws DataConversionException;
    
    public Integer deactivate(List<TestCaseVo> vos) throws DataConversionException;
    
    public Optional<TestCase> activate(TestCaseVo vo) throws DataConversionException;
    
    public Integer activate(List<TestCaseVo> vos) throws DataConversionException;

    public Optional<TestCase> delete(TestCaseVo vo) throws DataConversionException;
    
    public Integer delete(List<TestCaseVo> vos) throws DataConversionException;
}
