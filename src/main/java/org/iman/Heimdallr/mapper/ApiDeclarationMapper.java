/**
 * 
 */
package org.iman.Heimdallr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.TestCase;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ey
 *
 */
@Repository
@Mapper
public interface ApiDeclarationMapper extends BaseMapper<ApiDeclaration>{

    public int insert(ApiDeclaration pojo);
    
    public List<ApiDeclaration> selectBy(ApiDeclaration criteria);
    
    public List<TestCase> selectByPage(@Param("declaration")TestCase criteria, @Param("offset")Integer offset, @Param("capacity")Integer capacity);
    
    public Integer countByPage(ApiDeclaration criteria);
    
    public int deleteBy(ApiDeclaration criteria);
    
    public int stateSwitch(ApiDeclaration criteria);
    
    public int updateById(ApiDeclaration testCase);
}
