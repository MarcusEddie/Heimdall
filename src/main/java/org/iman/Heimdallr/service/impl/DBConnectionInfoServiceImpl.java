/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.TestDBConnectionInfoMapper;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ey
 *
 */
@Service
public class DBConnectionInfoServiceImpl implements DBConnectionInfoService {

    private static final Logger log = LoggerFactory.getLogger(DBConnectionInfoServiceImpl.class);

    @Autowired
    private TestDBConnectionInfoMapper testDBConnectionInfoMapper;

    @Override
    public DBConnectionInfo save(DBConnectionInfoVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("DBConnectionInfoVo is required");
        });
        DBConnectionInfo info = BeanUtils.copy(vo, DBConnectionInfo.class).get();
        testDBConnectionInfoMapper.insert(info);
        return info;
    }

    @Override
    public Optional<List<DBConnectionInfo>> getConnInfoByAppId(Long appId) {
        Optional.ofNullable(appId).orElseThrow(() -> {
            throw new IllegalArgumentException("appId is required");
        });
        
        DBConnectionInfo criteria = new DBConnectionInfo();
        criteria.setAppId(appId);
        List<DBConnectionInfo> rs = testDBConnectionInfoMapper.selectBy(criteria);
        return CollectionUtils.sizeIsEmpty(rs) ? Optional.empty() : Optional.of(rs);
    }

    @Override
    public Optional<DBConnectionInfo> getById(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> {
            throw new IllegalArgumentException("id is required");
        });
        DBConnectionInfo criteria = new DBConnectionInfo(id);
        List<DBConnectionInfo> rs = testDBConnectionInfoMapper.selectBy(criteria);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }
        
        return Optional.of(rs.get(0));
    }

}
