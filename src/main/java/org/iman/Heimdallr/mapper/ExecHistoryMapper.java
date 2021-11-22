package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.ExecHistory;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
@Mapper
public interface ExecHistoryMapper extends BaseMapper<ExecHistory> {

    public int insert(ExecHistory queue);

    public List<ExecHistory> selectBy(@Param("criteria") ExecHistory criteria);
    
    public int changeToDelay(ExecHistory criteria);

    public List<ExecHistory> selectById(ExecHistory testCase);

    public List<ExecHistory> selectByPage(@Param("criteria") ExecHistory criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(@Param("criteria") ExecHistory criteria);

    public int deleteBy(ExecHistory criteria);

    public int stateSwitch(ExecHistory criteria);

    public int updateById(ExecHistory testCase);

}
