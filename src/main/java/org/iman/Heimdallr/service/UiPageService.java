/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.UiPageVo;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface UiPageService {

    public UiPage saveOnePage(UiPageVo vo);
    
    public List<UiPage> getByParams(UiPageVo criteria);
    
    public Pagination<UiPage> getByParams(UiPageVo criteria, Page page) throws DataConversionException;
    public Optional<UiPage> getById(Long id);
    
    public Optional<UiPage> delete(UiPageVo vo) throws DataConversionException;
    
    public Integer delete(List<UiPageVo> vos) throws DataConversionException;
    
    public Optional<UiPage> activate(UiPageVo vo) throws DataConversionException;
    
    public Integer activate(List<UiPageVo> vos) throws DataConversionException;
    
    public Optional<UiPage> deactivate(UiPageVo vo) throws DataConversionException;
    
    public Integer deactivate(List<UiPageVo> vos) throws DataConversionException;
    
    public Optional<UiPage> updateById(UiPageVo vo) throws DataConversionException;
}
