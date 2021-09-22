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
}
