/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.UiPage;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface UiPageMapper extends BaseMapper<UiPage> {

    public int insert(UiPage pojo);

    public List<UiPage> selectBy(UiPage criteria);

    public List<UiPage> selectByPage(@Param("criteria") UiPage criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(UiPage criteria);

    public int deleteBy(UiPage criteria);

    public int stateSwitch(UiPage criteria);

    public int updateById(UiPage testCase);
}
