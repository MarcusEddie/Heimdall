/**
 * 
 */
package org.iman.Heimdallr.mapper;

import org.apache.ibatis.annotations.Mapper;
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
}
