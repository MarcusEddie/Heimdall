package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TestPlanVo;

public interface TestPlanService {

    public TestPlan save(TestPlanVo plan) throws DataConversionException;
    
    public Pagination<TestPlan> getByParams(TestPlanVo criteria, Page page) throws DataConversionException;
    
    public Pagination<ApiTestCase> getAPITestCasesByPlanId(TestPlanVo criteria, Page page) throws DataConversionException;
    
    public Optional<TestPlan> getById(Long id);
    
    public Optional<TestPlan> update(TestPlanVo plan) throws DataConversionException; 
    
    public Optional<TestPlan> delete(TestPlanVo vo) throws DataConversionException;
    
    public Integer delete(List<TestPlanVo> vos) throws DataConversionException;
    
    public Optional<TestPlan> activate(TestPlanVo vo) throws DataConversionException;
    
    public Integer activate(List<TestPlanVo> vos) throws DataConversionException;
    
    public Optional<TestPlan> deactivate(TestPlanVo vo) throws DataConversionException;
    
    public Integer deactivate(List<TestPlanVo> vos) throws DataConversionException;
}
