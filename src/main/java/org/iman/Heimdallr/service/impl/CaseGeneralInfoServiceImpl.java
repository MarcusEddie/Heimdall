/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.iman.Heimdallr.DataNotFoundException;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.mapper.CaseGeneralInfoMapper;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public TestCase saveOneCase(TestCaseVo vo) throws DataNotFoundException {
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
        
        TestCase testCase = null;
        try {
            testCase = BeanUtils.copy(vo, TestCase.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            if (log.isErrorEnabled()) {
                log.error("Convert TestCase from TestCaseVo failed because of", e);
            }
            throw new IllegalArgumentException();
        }
        caseGeneralInfoMapper.insert(testCase);
        
        return testCase;
    }

}
