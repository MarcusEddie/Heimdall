/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.UiTestCase;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface UiTestCaseDetailsMapper extends BaseMapper<UiTestCase> {

    public int insert(UiTestCase testCase);

    public List<UiTestCase> selectBy(@Param("criteria") UiTestCase criteria);
    
    public List<UiTestCase> selectById(UiTestCase testCase);

    public List<UiTestCase> selectByPage(@Param("criteria") UiTestCase criteria,
            @Param("apiIds") List<Long> apiIds, @Param("offset") Integer offset,
            @Param("capacity") Integer capacity);

    public Integer countByPage(@Param("criteria") UiTestCase criteria,
            @Param("apiIds") List<Long> generalCaseIds);

    public int deleteBy(UiTestCase criteria);

    public int stateSwitch(UiTestCase criteria);

    public int updateById(UiTestCase testCase);
    
    public int clearResultById(UiTestCase testCase);
    
    public int clearDBInfoById(UiTestCase testCase);
    
    public int clearHeadersById(UiTestCase testCase);

}
