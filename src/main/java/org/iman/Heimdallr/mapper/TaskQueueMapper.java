package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.TaskQueue;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
@Mapper
public interface TaskQueueMapper extends BaseMapper<TaskQueue> {

    public int insert(TaskQueue queue);

    public List<TaskQueue> selectBy(@Param("criteria") TaskQueue criteria);
    
    public int changeToDelay(TaskQueue criteria);

    public List<TaskQueue> selectById(TaskQueue testCase);

    public List<TaskQueue> selectByPage(@Param("criteria") TaskQueue criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(@Param("criteria") TaskQueue criteria);

    public int deleteBy(TaskQueue criteria);

    public int stateSwitch(TaskQueue criteria);

    public int updateById(TaskQueue testCase);

}
