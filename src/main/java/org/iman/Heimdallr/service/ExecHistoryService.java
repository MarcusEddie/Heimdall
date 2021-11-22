package org.iman.Heimdallr.service;

import java.util.Optional;

import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.ExecHistoryVo;
import org.iman.Heimdallr.vo.Pagination;

public interface ExecHistoryService {

    public ExecHistory save(ExecHistoryVo history) throws DataConversionException;
    
    public Optional<TaskQueue> reEnqueue(ExecHistoryVo history) throws DataConversionException;
    
    public Pagination<ExecHistory> getByParams(ExecHistoryVo criteria, Page page) throws DataConversionException;
    
    public Optional<ExecHistory> getById(Long id) throws DataConversionException;
}
