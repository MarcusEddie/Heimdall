/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.vo.ApiDeclarationVo;

/**
 * @author ey
 *
 */
public interface APIService {

    public ApiDeclaration saveOneAPI(ApiDeclarationVo vo);
    
    public List<ApiDeclaration> getByParams(ApiDeclarationVo criteria);
    
    public Optional<ApiDeclaration> getById(Long id);
}
