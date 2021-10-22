/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.DBConnection;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;
import org.iman.Heimdallr.vo.Pagination;

/**
 * @author ey
 *
 */
public interface DBConnectionInfoService {

    public DBConnectionInfo save(DBConnectionInfoVo vo) throws DataConversionException;

    public Optional<List<DBConnectionInfo>> getConnInfoByAppId(Long appId);

    public Optional<DBConnectionInfo> getById(Long id);

    public Pagination<DBConnectionInfo> getByParams(DBConnectionInfoVo criteria, Page page)
            throws DataConversionException;

    public Optional<DBConnectionInfo> delete(DBConnectionInfoVo vo) throws DataConversionException;

    public Integer delete(List<DBConnectionInfoVo> vos) throws DataConversionException;

    public Optional<DBConnectionInfo> activate(DBConnectionInfoVo vo)
            throws DataConversionException;

    public Integer activate(List<DBConnectionInfoVo> vos) throws DataConversionException;

    public Optional<DBConnectionInfo> deactivate(DBConnectionInfoVo vo)
            throws DataConversionException;

    public Integer deactivate(List<DBConnectionInfoVo> vos) throws DataConversionException;

    public Optional<DBConnectionInfo> updateById(DBConnectionInfoVo vo)
            throws DataConversionException;
    
    public DBConnection testDBConnection(DBConnectionInfoVo params);
}
