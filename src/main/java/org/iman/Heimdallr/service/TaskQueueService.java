package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TaskQueueVo;

public interface TaskQueueService {


    public TaskQueue save(TaskQueueVo vo) throws DataConversionException;
    
    public Pagination<TaskQueue> getByParams(TaskQueueVo criteria, Page page) throws DataConversionException;
    
    public Optional<TaskQueue> getById(Long id) throws DataConversionException;
    
    public Optional<ExecHistory> delete(TaskQueueVo vo) throws DataConversionException;
    
    public Integer delete(List<TaskQueueVo> vos) throws DataConversionException;
    
    public Optional<TaskQueue> activate(TaskQueueVo vo) throws DataConversionException;
    
    public Integer activate(List<TaskQueueVo> vos) throws DataConversionException;
    
    public Optional<TaskQueue> deactivate(TaskQueueVo vo) throws DataConversionException;
    
    public Integer deactivate(List<TaskQueueVo> vos) throws DataConversionException;
}
