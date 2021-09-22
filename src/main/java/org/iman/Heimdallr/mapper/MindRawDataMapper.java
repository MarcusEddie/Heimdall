/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.iman.Heimdallr.entity.MindRawData;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface MindRawDataMapper extends BaseMapper<MindRawData>{

    public int insert(MindRawData raw);
    
    public List<MindRawData> selectBy(MindRawData criteria);
}
