/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface APIService {

    public ApiDeclaration saveOneAPI(ApiDeclarationVo vo);
    
    public List<ApiDeclaration> getByParams(ApiDeclarationVo criteria);
    
    public Pagination<ApiDeclaration> getByParams(ApiDeclarationVo criteria, Page page) throws DataConversionException;
    public Optional<ApiDeclaration> getById(Long id);
    
    public Optional<ApiDeclaration> delete(ApiDeclarationVo vo) throws DataConversionException;
    
    public Integer delete(List<ApiDeclarationVo> vos) throws DataConversionException;
    
    public Optional<ApiDeclaration> activate(ApiDeclarationVo vo) throws DataConversionException;
    
    public Integer activate(List<ApiDeclarationVo> vos) throws DataConversionException;
    
    public Optional<ApiDeclaration> deactivate(ApiDeclarationVo vo) throws DataConversionException;
    
    public Integer deactivate(List<ApiDeclarationVo> vos) throws DataConversionException;
    
    public Optional<ApiDeclaration> updateById(ApiDeclarationVo vo) throws DataConversionException;
}
