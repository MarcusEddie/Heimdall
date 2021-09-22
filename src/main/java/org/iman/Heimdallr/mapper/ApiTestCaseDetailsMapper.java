/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface ApiTestCaseDetailsMapper extends BaseMapper<ApiTestCase> {

    public int insert(ApiTestCase testCase);

    public List<ApiTestCase> selectBy(ApiTestCase criteria);

    public List<ApiTestCase> selectByPage(@Param("criteria") ApiTestCase criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(ApiTestCase criteria);

    public int deleteBy(ApiTestCase criteria);

    public int stateSwitch(ApiTestCase criteria);

    public int updateById(ApiTestCase testCase);

}
