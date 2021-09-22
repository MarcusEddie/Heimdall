/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.exception.DataNotFoundException;
import org.iman.Heimdallr.mapper.CaseGeneralInfoMapper;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ey
 *
 */
@Service
public class CaseGeneralInfoServiceImpl implements CaseGeneralInfoService {

    private static final Logger log = LoggerFactory.getLogger(CaseGeneralInfoServiceImpl.class);

    @Autowired
    private CaseGeneralInfoMapper caseGeneralInfoMapper;
    @Autowired
    private AppStructureService appStructureService;

    @Override
    public Optional<TestCase> saveOneCase(TestCaseVo vo) throws DataNotFoundException {
        Optional.ofNullable(vo.getFunctionId()).orElseThrow(() -> {
            throw new IllegalArgumentException("FunctionId is required");
        });

        Optional<AppStructure> function = appStructureService.getById(vo.getFunctionId());
        if (function.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Function is not found with a given functionId");
            }
            throw new DataNotFoundException();
        }
        vo.setModuleId(function.get().getRoot());
        Optional<AppStructure> module = appStructureService.getById(vo.getModuleId());
        if (module.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Module is not found with a given moduleId");
            }
            throw new DataNotFoundException();
        }
        vo.setAppId(module.get().getRoot());
        vo.setRawDataId(0L);

        TestCase rs = save0(vo);

        return Optional.ofNullable(rs);
    }

    @Override
    @Transactional
    public Integer saveMultiCases(List<TestCaseVo> cases) {
        if (CollectionUtils.sizeIsEmpty(cases)) {
            return 0;
        }

        AtomicInteger cnt = new AtomicInteger(0);
        Iterator<TestCaseVo> it = cases.iterator();
        while (it.hasNext()) {
            TestCaseVo vo = (TestCaseVo) it.next();
            save0(vo);
            cnt.addAndGet(1);
        }

        return cnt.get();
    }

    @Override
    public List<TestCase> getByParams(TestCaseVo vo) throws DataConversionException {
        Map<Long, String> names = new HashMap<Long, String>();
        Optional<TestCase> cpObj = BeanUtils.copy(vo, TestCase.class);
        if (cpObj.isEmpty()) {
            return Collections.emptyList();
        }

        TestCase testCase = cpObj.get();
        testCase.setEnabled(TestCaseState.convertToBoolean(vo.getState()));
        List<TestCase> rs = caseGeneralInfoMapper.selectBy(testCase);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Collections.emptyList();
        }

        Optional<AppStructure> appRs = appStructureService.getById(vo.getAppId());
        String appName = appRs.isPresent() ? appRs.get().getName() : "";

        String moduleName = "";
        Boolean moduleNameFilled = false;
        if (null != vo.getModuleId() && vo.getModuleId().compareTo(0L) > 0) {
            Optional<AppStructure> module = appStructureService.getById(vo.getModuleId());
            moduleName = module.isPresent() ? module.get().getName() : "";
            moduleNameFilled = true;
        }

        String funcName = "";
        Boolean funcNameFilled = false;
        if (null != vo.getModuleId() && vo.getModuleId().compareTo(0L) > 0) {
            Optional<AppStructure> func = appStructureService.getById(vo.getFunctionId());
            funcName = func.isPresent() ? func.get().getName() : "";
            funcNameFilled = true;
        }

        Iterator<TestCase> it = rs.iterator();
        List<TestCase> result = new ArrayList<TestCase>();
        while (it.hasNext()) {
            TestCase testCase2 = (TestCase) it.next();
            TestCase case1 = BeanUtils.copy(testCase2, TestCase.class).get();

            case1.setAppName(appName);
            if (!moduleNameFilled) {
                if (names.containsKey(testCase2.getModuleId())) {
                    moduleName = names.get(testCase2.getModuleId());
                } else {
                    Optional<AppStructure> module = appStructureService
                            .getById(testCase2.getModuleId());
                    moduleName = module.isPresent() ? module.get().getName() : "";
                    names.put(module.get().getId(), module.get().getName());
                }
            }
            case1.setModuleName(moduleName);

            if (!funcNameFilled) {
                if (names.containsKey(testCase2.getFunctionId())) {
                    funcName = names.get(testCase2.getFunctionId());
                } else {
                    Optional<AppStructure> func = appStructureService
                            .getById(testCase2.getFunctionId());
                    funcName = func.isPresent() ? func.get().getName() : "";
                    names.put(func.get().getId(), func.get().getName());
                }
            }
            case1.setFunctionName(funcName);
            result.add(case1);
        }

        return result;
    }

    @Override
    public Pagination<TestCase> getByParams(TestCaseVo vo, Page page)
            throws DataConversionException {
        Pagination<TestCase> rs = new Pagination<TestCase>(page.getCurrent(), page.getPageSize());
        Map<Long, String> names = new HashMap<Long, String>();
        Optional<TestCase> cpObj = BeanUtils.copy(vo, TestCase.class);
        if (cpObj.isEmpty()) {
            return new Pagination<TestCase>(Collections.emptyList(), page.getCurrent(),
                    page.getPageSize(), 0);
        }

        TestCase testCase = cpObj.get();
        testCase.setEnabled(TestCaseState.convertToBoolean(vo.getState()));
        List<TestCase> cases = caseGeneralInfoMapper.selectByPage(testCase, page.getOffset(), page.getCapacity());
        Integer totalCnt = caseGeneralInfoMapper.countByPage(testCase);
        if (CollectionUtils.sizeIsEmpty(cases)) {
            return new Pagination<TestCase>(Collections.emptyList(), page.getCurrent(),
                    page.getPageSize(), 0);
        }

        rs.setTotal(totalCnt);
        Optional<AppStructure> appRs = appStructureService.getById(vo.getAppId());
        String appName = appRs.isPresent() ? appRs.get().getName() : "";

        String moduleName = "";
        Boolean moduleNameFilled = false;
        if (null != vo.getModuleId() && vo.getModuleId().compareTo(0L) > 0) {
            Optional<AppStructure> module = appStructureService.getById(vo.getModuleId());
            moduleName = module.isPresent() ? module.get().getName() : "";
            moduleNameFilled = true;
        }

        String funcName = "";
        Boolean funcNameFilled = false;
        if (null != vo.getModuleId() && vo.getModuleId().compareTo(0L) > 0) {
            Optional<AppStructure> func = appStructureService.getById(vo.getFunctionId());
            funcName = func.isPresent() ? func.get().getName() : "";
            funcNameFilled = true;
        }

        Iterator<TestCase> it = cases.iterator();
        List<TestCase> casesTemp = new ArrayList<TestCase>();
        while (it.hasNext()) {
            TestCase testCase2 = (TestCase) it.next();
            TestCase case1 = BeanUtils.copy(testCase2, TestCase.class).get();

            case1.setAppName(appName);
            if (!moduleNameFilled) {
                if (names.containsKey(testCase2.getModuleId())) {
                    moduleName = names.get(testCase2.getModuleId());
                } else {
                    Optional<AppStructure> module = appStructureService
                            .getById(testCase2.getModuleId());
                    moduleName = module.isPresent() ? module.get().getName() : "";
                    names.put(module.get().getId(), module.get().getName());
                }
            }
            case1.setModuleName(moduleName);

            if (!funcNameFilled) {
                if (names.containsKey(testCase2.getFunctionId())) {
                    funcName = names.get(testCase2.getFunctionId());
                } else {
                    Optional<AppStructure> func = appStructureService
                            .getById(testCase2.getFunctionId());
                    funcName = func.isPresent() ? func.get().getName() : "";
                    names.put(func.get().getId(), func.get().getName());
                }
            }
            case1.setFunctionName(funcName);
            casesTemp.add(case1);
        }

        rs.setList(casesTemp);
        return rs;
    }
    
    @Override
    public Optional<TestCase> updateTestCase(TestCaseVo vo) throws DataConversionException {
        Optional<TestCase> testCaseRs = BeanUtils.copy(vo, TestCase.class);
        if (testCaseRs.isEmpty()) {
            return Optional.empty();
        }
        
        TestCase testCase = testCaseRs.get();
        caseGeneralInfoMapper.updateById(testCase);
        return Optional.of(testCase);
    }

    @Override
    public Optional<TestCase> deactivate(TestCaseVo vo) throws DataConversionException {
        Optional<TestCase> cpObj = BeanUtils.copy(vo, TestCase.class);
        TestCase testCase = cpObj.get();
        testCase.setEnabled(false);
        caseGeneralInfoMapper.stateSwitch(testCase);
        return Optional.of(testCase);
    }
    @Override
    @Transactional
    public Integer deactivate(List<TestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<TestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            TestCaseVo vo = (TestCaseVo) it.next();
            deactivate(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<TestCase> activate(TestCaseVo vo) throws DataConversionException {
        Optional<TestCase> cpObj = BeanUtils.copy(vo, TestCase.class);
        TestCase testCase = cpObj.get();
        testCase.setEnabled(true);
        caseGeneralInfoMapper.stateSwitch(testCase);
        return Optional.of(testCase);
    }

    @Override
    @Transactional
    public Integer activate(List<TestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<TestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            TestCaseVo vo = (TestCaseVo) it.next();
            activate(vo);
        }
        
        return vos.size();
    }
    
    @Override
    public Optional<TestCase> delete(TestCaseVo vo)  throws DataConversionException {
        Optional<TestCase> cpObj = BeanUtils.copy(vo, TestCase.class);
        TestCase testCase = cpObj.get();
        caseGeneralInfoMapper.deleteBy(testCase);
        return Optional.of(testCase);
    }

    @Override
    @Transactional
    public Integer delete(List<TestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<TestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            TestCaseVo vo = (TestCaseVo) it.next();
            delete(vo);
        }
        
        return vos.size();
    }

    private TestCase save0(TestCaseVo vo) {
        try {
            Optional<TestCase> testCase = BeanUtils.copy(vo, TestCase.class);
            TestCase oneCase = testCase.get();
            caseGeneralInfoMapper.insert(oneCase);
            return oneCase;
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Conversion failed", e);
            }
            throw new IllegalArgumentException();
        }
    }

}
