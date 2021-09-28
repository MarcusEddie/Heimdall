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

    public List<ApiTestCase> selectBy(@Param("criteria") ApiTestCase criteria,
            @Param("apiIds") List<Long> generalCaseIds);

    public List<ApiTestCase> selectByPage(@Param("criteria") ApiTestCase criteria,
            @Param("apiIds") List<Long> apiIds, @Param("offset") Integer offset,
            @Param("capacity") Integer capacity);

    public Integer countByPage(@Param("criteria") ApiTestCase criteria,
            @Param("apiIds") List<Long> generalCaseIds);

    public int deleteBy(ApiTestCase criteria);

    public int stateSwitch(ApiTestCase criteria);

    public int updateById(ApiTestCase testCase);
    
    public int clearResultById(ApiTestCase testCase);
    
    public int clearDBInfoById(ApiTestCase testCase);
    
    public int clearHeadersById(ApiTestCase testCase);

}
