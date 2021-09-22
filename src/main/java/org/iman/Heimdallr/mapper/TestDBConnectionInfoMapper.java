/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface TestDBConnectionInfoMapper extends BaseMapper<DBConnectionInfo> {

    public int insert(DBConnectionInfo connInfo);

    public List<DBConnectionInfo> selectBy(DBConnectionInfo criteria);

    public List<DBConnectionInfo> selectByPage(@Param("connInfo") DBConnectionInfo criteria,
            @Param("offset") Integer offset, @Param("capacity") Integer capacity);

    public Integer countByPage(DBConnectionInfo criteria);

    public int deleteBy(DBConnectionInfo criteria);

    public int stateSwitch(DBConnectionInfo criteria);

    public int updateById(DBConnectionInfo testCase);
}
