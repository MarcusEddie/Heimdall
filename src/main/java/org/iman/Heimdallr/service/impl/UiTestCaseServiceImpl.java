/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ApiTestCaseDetailsMapper;
import org.iman.Heimdallr.mapper.UiTestCaseDetailsMapper;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.ApiTestCaseService;
import org.iman.Heimdallr.service.DataHistoryService;
import org.iman.Heimdallr.service.UiPageService;
import org.iman.Heimdallr.service.UiTestCaseService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.UiTestCaseVo;
import org.iman.Heimdallr.vo.Pagination;
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
public class UiTestCaseServiceImpl implements UiTestCaseService {

    private static final Logger log = LoggerFactory.getLogger(UiTestCaseServiceImpl.class);

    @Autowired
    private UiTestCaseDetailsMapper uiTestCaseDetailsMapper;
    @Autowired
    private UiPageService uiPageService;
    @Autowired
    private DataHistoryService dataHistoryService;

    @Override
    @Transactional
    public UiTestCase save(UiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiTestCaseVo is required");
        });

        Optional<UiTestCase> cpObj = BeanUtils.copy(vo, UiTestCase.class);
        UiTestCase testCase = cpObj.get();
        Optional<UiPage> api = uiPageService.getById(testCase.getPageId());
        if (api.isPresent()) {
            testCase.setAppId(api.get().getAppId());
        }
        uiTestCaseDetailsMapper.insert(testCase);

        dataHistoryService.save(null, testCase, testCase.getId(), Action.CREATE,
                FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        
        return testCase;
    }

    @Override
    public Optional<UiTestCase> getById(Long id) throws DataConversionException {
        Optional.ofNullable(id).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        UiTestCase criteria = new UiTestCase(id);
        List<UiTestCase> testCase= uiTestCaseDetailsMapper.selectById(criteria);
        if (CollectionUtils.sizeIsEmpty(testCase)) {
            return Optional.empty();
        }
        
        return Optional.of(testCase.get(0));
    }
    
    @Override
    public Pagination<UiTestCase> getByParams(UiTestCaseVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<UiTestCase> rs = new Pagination<UiTestCase>(page.getCurrent(),
                page.getPageSize());
        List<Long> apis = new ArrayList<Long>();
//        if (null != criteria.getPageId()) {
//            apis.addAll(getGeneralCaseId(criteria.getApi(), criteria.getAppId()));
//        }

        UiTestCase testCase = BeanUtils.copy(criteria, UiTestCase.class).get();
        testCase.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<UiTestCase> cases = uiTestCaseDetailsMapper.selectByPage(testCase, apis,
                page.getOffset(), page.getCapacity());
        rs.setList(cases);
        Integer cnt = uiTestCaseDetailsMapper.countByPage(testCase, apis);
        rs.setTotal(cnt);

        return rs;
    }
    
    @Override
    @Transactional
    public UiTestCase update(UiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiTestCaseVo is required");
        });
        UiTestCase testCase = BeanUtils.copy(vo, UiTestCase.class).get();
        Optional<UiTestCase> oldObj = getById(vo.getId());
        
        dataHistoryService.save(oldObj.get(), testCase, testCase.getId(), Action.UPDATE,
                FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        
        uiTestCaseDetailsMapper.updateById(testCase);
        
        return testCase;
    }

    @Override
    public Optional<UiTestCase> delete(UiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiTestCaseVo is required");
        });
        
        UiTestCase testCase = BeanUtils.copy(vo, UiTestCase.class).get();
        Optional<UiTestCase> oldObj = getById(vo.getId());
        dataHistoryService.save(oldObj.get(), null, oldObj.get().getId(), Action.DELETE,
                FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        
        uiTestCaseDetailsMapper.deleteBy(testCase);
        
        return Optional.of(testCase);
    }
    
    @Override
    @Transactional
    public Integer delete(List<UiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<UiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            UiTestCaseVo vo = (UiTestCaseVo) it.next();
            delete(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<UiTestCase> activate(UiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiTestCaseVo is required");
        });
        
        Optional<UiTestCase> oldObj = getById(vo.getId());
        
        UiTestCase testCase = BeanUtils.copy(vo, UiTestCase.class).get();
        testCase.setEnabled(true);
        dataHistoryService.save(oldObj.get(), testCase, testCase.getId(), Action.UPDATE,
                FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        uiTestCaseDetailsMapper.stateSwitch(testCase);
        
        return Optional.of(testCase);
    }

    @Override
    public Integer activate(List<UiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<UiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            UiTestCaseVo vo = (UiTestCaseVo) it.next();
            activate(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<UiTestCase> deactivate(UiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiTestCaseVo is required");
        });
        
        Optional<UiTestCase> oldObj = getById(vo.getId());
        UiTestCase testCase = BeanUtils.copy(vo, UiTestCase.class).get();
        testCase.setEnabled(false);
        dataHistoryService.save(oldObj.get(), testCase, testCase.getId(), Action.UPDATE,
                FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        uiTestCaseDetailsMapper.stateSwitch(testCase);
        
        return Optional.of(testCase);
    }
    
    @Override
    public Integer deactivate(List<UiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<UiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            UiTestCaseVo vo = (UiTestCaseVo) it.next();
            deactivate(vo);
        }
        
        return vos.size();
    }

//    private List<Long> getGeneralCaseId(ApiDeclarationVo api, Long appId) throws DataConversionException {
//        List<Long> rs = new ArrayList<Long>();
//        if (null != api
//                && ((null != api.getModuleId() && Long.valueOf(0L).compareTo(api.getModuleId()) < 0))
//                || (null != api.getFunctionId() && Long.valueOf(0L).compareTo(api.getFunctionId()) < 0)) {
//            api.setAppId(appId);
//            Page page = new Page();
//            page.setPageSize(Consts.PAGE_SIZE_50);
//            Boolean eof = false;
//            int cnt = 1;
//            while (!eof) {
//                page.setCurrent(cnt);
//                Pagination<ApiDeclaration> apis = uiPageService.getByParams(api, page);
//                eof = page.getPageSize().equals(apis.getList().size()) ? false : true;
//                cnt += 1;
//
//                if (!CollectionUtils.sizeIsEmpty(apis.getList())) {
//                    Iterator<ApiDeclaration> it = apis.getList().iterator();
//                    while (it.hasNext()) {
//                        ApiDeclaration case2 = (ApiDeclaration) it.next();
//                        rs.add(case2.getId());
//                    }
//                }
//            }
//            
//            rs.add(0L);
//        }
//
//        return rs;
//    }

}
