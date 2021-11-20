/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.TestPlan;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface TestPlanMapper extends BaseMapper<TestPlan> {

    public int insert(TestPlan testCase);

    public List<TestPlan> selectNonRepeatPlanBeforeEndTime(@Param("criteria") TestPlan criteria);
    
    public List<TestPlan> selectBy(@Param("criteria") TestPlan criteria);

    public List<TestPlan> selectByPage(@Param("criteria") TestPlan criteria, @Param("offset") Integer offset,
            @Param("capacity") Integer capacity);

    public Integer countByPage(@Param("criteria") TestPlan criteria);

    public List<TestPlan> selectById(TestPlan plan);
    
    public int deleteBy(TestPlan criteria);

    public int stateSwitch(TestPlan criteria);

    public int updateById(TestPlan testCase);

}
