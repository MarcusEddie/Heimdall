package org.iman.Heimdallr.service;

import org.iman.Heimdallr.entity.ExecHistoryFailureDetails;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.ExecHistoryFailureDetailsVo;
import org.iman.Heimdallr.vo.Pagination;

public interface ExecHistoryFailureDetailsService {

    public ExecHistoryFailureDetails save(ExecHistoryFailureDetailsVo details)
            throws DataConversionException;

    public Pagination<ExecHistoryFailureDetails> getByParams(ExecHistoryFailureDetailsVo criteria,
            Page page) throws DataConversionException;
}
