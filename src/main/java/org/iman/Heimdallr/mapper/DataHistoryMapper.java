package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.DataHistory;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
@Mapper
public interface DataHistoryMapper extends BaseMapper<DataHistory>{
    
    public int insert(DataHistory history);
    
    public List<DataHistory> selectBy(DataHistory req);
    
    public List<DataHistory> selectByPage(@Param("criteria") DataHistory criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(DataHistory criteria);
    
}
