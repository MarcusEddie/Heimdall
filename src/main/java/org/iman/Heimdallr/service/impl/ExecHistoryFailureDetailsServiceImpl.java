package org.iman.Heimdallr.service.impl;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ExecHistoryFailureDetails;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ExecHistoryFailureDetailsMapper;
import org.iman.Heimdallr.service.ExecHistoryFailureDetailsService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ExecHistoryFailureDetailsVo;
import org.iman.Heimdallr.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecHistoryFailureDetailsServiceImpl implements ExecHistoryFailureDetailsService {

    @Autowired
    private ExecHistoryFailureDetailsMapper execHistoryFailureDetailsMapper;

    @Override
    public ExecHistoryFailureDetails save(ExecHistoryFailureDetailsVo details)
            throws DataConversionException {

        ExecHistoryFailureDetails failureDetails = BeanUtils
                .copy(details, ExecHistoryFailureDetails.class).get();
        execHistoryFailureDetailsMapper.insert(failureDetails);
        return failureDetails;
    }

    @Override
    public Pagination<ExecHistoryFailureDetails> getByParams(ExecHistoryFailureDetailsVo criteria,
            Page page) throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("ExecHistoryFailureDetailsVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<ExecHistoryFailureDetails> rs = new Pagination<ExecHistoryFailureDetails>(
                page.getCurrent(), page.getPageSize());
        ExecHistoryFailureDetails details = BeanUtils
                .copy(criteria, ExecHistoryFailureDetails.class).get();
        details.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));

        List<ExecHistoryFailureDetails> histories = execHistoryFailureDetailsMapper
                .selectByPage(details, page.getOffset(), page.getCapacity());
        rs.setList(histories);
        Integer cnt = execHistoryFailureDetailsMapper.countByPage(details);
        rs.setTotal(cnt);

        return rs;
    }

}
