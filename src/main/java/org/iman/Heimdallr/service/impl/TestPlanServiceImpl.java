/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.TestPlanMapper;
import org.iman.Heimdallr.service.ApiTestCaseService;
import org.iman.Heimdallr.service.TestPlanService;
import org.iman.Heimdallr.service.UiTestCaseService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.TimeUtils;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TestPlanVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@Service
public class TestPlanServiceImpl implements TestPlanService {

    private static final Logger log = LoggerFactory.getLogger(TestPlanServiceImpl.class);

    @Autowired
    private TestPlanMapper testPlanMapper;
    @Autowired
    private ApiTestCaseService apiTestCaseService;
    @Autowired
    private UiTestCaseService uiTestCaseService;
    
    @Override
    public TestPlan save(TestPlanVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });
        
        TestPlan plan = BeanUtils.copy(vo, TestPlan.class).get();
        
        if (null == plan.getTriggerTime()) {
            plan.setRepeatFlag(true);
        } else {
            plan.setRepeatFlag(false);
        }
        Iterator<JsonNode> it = plan.getCaseSet().iterator();
        int cnt = 0;
        while (it.hasNext()) {
            JsonNode node = (JsonNode) it.next();
            cnt++;
        }
        plan.setCaseSize(cnt);
        
        
        testPlanMapper.insert(plan);
        
        return plan;
    }

    @Override
    public Pagination<TestPlan> getByParams(TestPlanVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<TestPlan> rs = new Pagination<TestPlan>(page.getCurrent(), page.getPageSize());
        TestPlan plan = BeanUtils.copy(criteria, TestPlan.class).get();
        plan.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<TestPlan> plans = testPlanMapper.selectByPage(plan, page.getOffset(),
                page.getCapacity());
        if (!CollectionUtils.sizeIsEmpty(plans)) {
            Iterator<TestPlan> it = plans.iterator();
            while (it.hasNext()) {
                TestPlan testPlan = (TestPlan) it.next();
                if (testPlan.getRepeatFlag()) {
                    testPlan.setNextTriggerTime(TimeUtils.calculateNextTriggerTime(testPlan.getCron()));
                }
            }
        }
        rs.setList(plans);
        Integer cnt = testPlanMapper.countByPage(plan);
        rs.setTotal(cnt);

        return rs;
    }
    
    @Override
    public Pagination<ApiTestCase> getAPITestCasesByPlanId(TestPlanVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });
        TestPlan plan = getById(criteria.getId()).get();
        Pagination<ApiTestCase> rs = new Pagination<ApiTestCase>(page.getCurrent(),
                page.getPageSize());
        Iterator<JsonNode> it = plan.getCaseSet().iterator();
        List<Long> caseIds = new ArrayList<Long>();
        while (it.hasNext()) {
            JsonNode jsonNode = (JsonNode) it.next();
            caseIds.add(jsonNode.asLong());
        }
        rs.setTotal(caseIds.size());
        
        caseIds = caseIds.subList(page.getOffset(), caseIds.size());
        if (caseIds.size() > page.getCapacity()) {
            caseIds = caseIds.subList(0, caseIds.size());
        }
        
        List<ApiTestCase> testCases = apiTestCaseService.getByIds(caseIds);
        rs.setList(testCases);

        return rs;
    }
    
    @Override
    public Pagination<UiTestCase> getUiTestCaseByPlanId(TestPlanVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });
        TestPlan plan = getById(criteria.getId()).get();
        Pagination<UiTestCase> rs = new Pagination<UiTestCase>(page.getCurrent(),
                page.getPageSize());
        Iterator<JsonNode> it = plan.getCaseSet().iterator();
        List<Long> caseIds = new ArrayList<Long>();
        while (it.hasNext()) {
            JsonNode jsonNode = (JsonNode) it.next();
            caseIds.add(jsonNode.asLong());
        }
        rs.setTotal(caseIds.size());
        
        caseIds = caseIds.subList(page.getOffset(), caseIds.size());
        if (caseIds.size() > page.getCapacity()) {
            caseIds = caseIds.subList(0, caseIds.size());
        }
        
        List<UiTestCase> testCases = uiTestCaseService.getByIds(caseIds);
        rs.setList(testCases);

        return rs;
    }
    
    @Override
    public Optional<TestPlan> getById(Long id) {
        TestPlan plan = new TestPlan(id);
        List<TestPlan> plans = testPlanMapper.selectById(plan);
        
        if (CollectionUtils.sizeIsEmpty(plans)) {
            return Optional.empty();
        }
        
        TestPlan rs = plans.get(0);
        if (rs.getRepeatFlag()) {
            rs.setNextTriggerTime(TimeUtils.calculateNextTriggerTime(rs.getCron()));
        }
        
        return Optional.of(rs);
    }

    @Override
    public Optional<TestPlan> update(TestPlanVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });

        TestPlan plan = BeanUtils.copy(vo, TestPlan.class).get();
        testPlanMapper.updateById(plan);
        
        return Optional.of(plan);
    }

    @Override
    public Optional<TestPlan> delete(TestPlanVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });

        TestPlan plan = BeanUtils.copy(vo, TestPlan.class).get();
        testPlanMapper.deleteBy(plan);

        return Optional.of(plan);
    }

    @Override
    public Integer delete(List<TestPlanVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TestPlanVo> it = vos.iterator();
        while (it.hasNext()) {
            TestPlanVo vo = (TestPlanVo) it.next();
            delete(vo);
        }

        return vos.size();
    }

    @Override
    public Optional<TestPlan> activate(TestPlanVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });

        TestPlan plan = BeanUtils.copy(vo, TestPlan.class).get();
        plan.setEnabled(true);
        testPlanMapper.stateSwitch(plan);

        return Optional.of(plan);
    }

    @Override
    @Transactional
    public Integer activate(List<TestPlanVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TestPlanVo> it = vos.iterator();
        while (it.hasNext()) {
            TestPlanVo vo = (TestPlanVo) it.next();
            activate(vo);
        }

        return vos.size();
    }

    @Override
    public Optional<TestPlan> deactivate(TestPlanVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });

        TestPlan plan = BeanUtils.copy(vo, TestPlan.class).get();
        plan.setEnabled(false);
        testPlanMapper.stateSwitch(plan);

        return Optional.of(plan);
    }

    @Override
    @Transactional
    public Integer deactivate(List<TestPlanVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TestPlanVo> it = vos.iterator();
        while (it.hasNext()) {
            TestPlanVo vo = (TestPlanVo) it.next();
            deactivate(vo);
        }

        return vos.size();
    }
}
