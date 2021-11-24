package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.ExecHistoryFailureDetails;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
@Mapper
public interface ExecHistoryFailureDetailsMapper extends BaseMapper<ExecHistoryFailureDetails>{

    public int insert(ExecHistoryFailureDetails details);
    
    public List<ExecHistoryFailureDetails> selectByPage(@Param("criteria") ExecHistoryFailureDetails criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(ExecHistoryFailureDetails criteria);
    
}
