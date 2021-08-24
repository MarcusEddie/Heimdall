/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.iman.Heimdallr.entity.AppStructure;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface AppStructureMapper extends BaseMapper<AppStructure> {

    public int insert(AppStructure obj);

    public List<AppStructure> selectBy(AppStructure query);

}
