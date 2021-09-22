/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ApiTestCaseDetailsMapper;
import org.iman.Heimdallr.service.ApiTestCaseService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ApiTestCaseVo;
import org.iman.Heimdallr.vo.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ey
 *
 */
@Service
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    private static final Logger log = LoggerFactory.getLogger(ApiTestCaseServiceImpl.class);

    @Autowired
    private ApiTestCaseDetailsMapper apiTestCaseDetailsMapper;

    @Override
    public ApiTestCase save(ApiTestCaseVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiTestCaseVo is required");
        });

        Optional<ApiTestCase> cpObj = BeanUtils.copy(vo, ApiTestCase.class);
        ApiTestCase testCase = cpObj.get();
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

        ApiTestCase testCase = BeanUtils.copy(criteria, ApiTestCase.class).get();
        List<ApiTestCase> cases = apiTestCaseDetailsMapper.selectByPage(testCase, page.getOffset(),
                page.getCapacity());
        rs.setList(cases);
        Integer cnt = apiTestCaseDetailsMapper.countByPage(testCase);
        rs.setTotal(cnt);

        return rs;
    }

}
