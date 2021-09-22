/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;

/**
 * @author ey
 *
 */
public interface DBConnectionInfoService {

    public DBConnectionInfo save(DBConnectionInfoVo vo) throws DataConversionException ;
    
    public Optional<List<DBConnectionInfo>> getConnInfoByAppId(Long appId);
    
    public Optional<DBConnectionInfo> getById(Long id);
    
}
