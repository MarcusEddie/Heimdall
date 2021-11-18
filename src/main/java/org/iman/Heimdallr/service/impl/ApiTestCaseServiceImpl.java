/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ApiTestCaseDetailsMapper;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.ApiTestCaseService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.ApiTestCaseVo;
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
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    private static final Logger log = LoggerFactory.getLogger(ApiTestCaseServiceImpl.class);

    @Autowired
    private ApiTestCaseDetailsMapper apiTestCaseDetailsMapper;
    @Autowired
    private APIService apiService;

    @Override
    public ApiTestCase save(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });

        Optional<ApiTestCase> cpObj = BeanUtils.copy(vo, ApiTestCase.class);
        ApiTestCase testCase = cpObj.get();
        Optional<ApiDeclaration> api = apiService.getById(testCase.getApiId());
        if (api.isPresent()) {
            testCase.setAppId(api.get().getAppId());
        }
        apiTestCaseDetailsMapper.insert(testCase);

        return testCase;
    }

    @Override
    public Pagination<ApiTestCase> getByParams(ApiTestCaseVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<ApiTestCase> rs = new Pagination<ApiTestCase>(page.getCurrent(),
                page.getPageSize());
        List<Long> apis = new ArrayList<Long>();
        if (null != criteria.getApi()) {
            apis.addAll(getGeneralCaseId(criteria.getApi(), criteria.getAppId()));
        }

        ApiTestCase testCase = BeanUtils.copy(criteria, ApiTestCase.class).get();
        testCase.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<ApiTestCase> cases = apiTestCaseDetailsMapper.selectByPage(testCase, apis,
                page.getOffset(), page.getCapacity());
        rs.setList(cases);
        Integer cnt = apiTestCaseDetailsMapper.countByPage(testCase, apis);
        rs.setTotal(cnt);

        return rs;
    }
    
    @Override
    public List<ApiTestCase> getByIds(List<Long> ids) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        
        List<ApiTestCase> testCases = apiTestCaseDetailsMapper.selectBatchIds(ids);
        return testCases;
    }
    
    @Override
    @Transactional
    public ApiTestCase update(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });

        ApiTestCase testCase = BeanUtils.copy(vo, ApiTestCase.class).get();
        apiTestCaseDetailsMapper.updateById(testCase);
        
        if (ResultCheckMode.RESPONSE_DATA.equals(testCase.getResultCheckMode())) {
            apiTestCaseDetailsMapper.clearDBInfoById(testCase);
        } else {
            apiTestCaseDetailsMapper.clearResultById(testCase);
        }
        if (null != testCase.getHeader() && (null != testCase.getHeader().get("headers") && testCase.getHeader().get("headers").isEmpty()) ) {
            apiTestCaseDetailsMapper.clearHeadersById(testCase);
        }
        
        return testCase;
    }

    @Override
    public Optional<ApiTestCase> delete(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });
        
        ApiTestCase testCase = BeanUtils.copy(vo, ApiTestCase.class).get();
        apiTestCaseDetailsMapper.deleteBy(testCase);
        
        return Optional.of(testCase);
    }
    
    @Override
    @Transactional
    public Integer delete(List<ApiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<ApiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiTestCaseVo vo = (ApiTestCaseVo) it.next();
            delete(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<ApiTestCase> activate(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });
        
        ApiTestCase testCase = BeanUtils.copy(vo, ApiTestCase.class).get();
        testCase.setEnabled(true);
        apiTestCaseDetailsMapper.stateSwitch(testCase);
        
        return Optional.of(testCase);
    }

    @Override
    @Transactional
    public Integer activate(List<ApiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<ApiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiTestCaseVo vo = (ApiTestCaseVo) it.next();
            activate(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<ApiTestCase> deactivate(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });
        
        ApiTestCase testCase = BeanUtils.copy(vo, ApiTestCase.class).get();
        testCase.setEnabled(false);
        apiTestCaseDetailsMapper.stateSwitch(testCase);
        
        return Optional.of(testCase);
    }
    
    @Override
    @Transactional
    public Integer deactivate(List<ApiTestCaseVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<ApiTestCaseVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiTestCaseVo vo = (ApiTestCaseVo) it.next();
            deactivate(vo);
        }
        
        return vos.size();
    }
    
    private List<Long> getGeneralCaseId(ApiDeclarationVo api, Long appId) throws DataConversionException {
        List<Long> rs = new ArrayList<Long>();
        if (null != api
                && ((null != api.getModuleId() && Long.valueOf(0L).compareTo(api.getModuleId()) < 0))
                || (null != api.getFunctionId() && Long.valueOf(0L).compareTo(api.getFunctionId()) < 0)) {
            api.setAppId(appId);
            Page page = new Page();
            page.setPageSize(Consts.PAGE_SIZE_50);
            Boolean eof = false;
            int cnt = 1;
            while (!eof) {
                page.setCurrent(cnt);
                Pagination<ApiDeclaration> apis = apiService.getByParams(api, page);
                eof = page.getPageSize().equals(apis.getList().size()) ? false : true;
                cnt += 1;

                if (!CollectionUtils.sizeIsEmpty(apis.getList())) {
                    Iterator<ApiDeclaration> it = apis.getList().iterator();
                    while (it.hasNext()) {
                        ApiDeclaration case2 = (ApiDeclaration) it.next();
                        rs.add(case2.getId());
                    }
                }
            }
            
            rs.add(0L);
        }

        return rs;
    }

}
