/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.TestCase;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface CaseGeneralInfoMapper extends BaseMapper<TestCase>{

    public int insert(TestCase testCase);
    
    public List<TestCase> selectBy(TestCase criteria);
    
    public List<TestCase> selectByPage(@Param("testCase")TestCase criteria, @Param("offset")Integer offset, @Param("capacity")Integer capacity);
    
    public Integer countByPage(TestCase criteria);
    
    public int deleteBy(TestCase criteria);
    
    public int stateSwitch(TestCase criteria);
    
    public int updateById(TestCase testCase);
}
