/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.TestDBConnectionInfoMapper;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.DBConnection;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;
import org.iman.Heimdallr.vo.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Pagination<DBConnectionInfo> getByParams(DBConnectionInfoVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<DBConnectionInfo> rs = new Pagination<DBConnectionInfo>(page.getCurrent(),
                page.getPageSize());
        DBConnectionInfo connInfo = convertToApiDeclaration(criteria);
        connInfo.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));

        List<DBConnectionInfo> conns = testDBConnectionInfoMapper.selectByPage(connInfo,
                page.getOffset(), page.getCapacity());
        rs.setList(conns);
        int cnt = testDBConnectionInfoMapper.countByPage(connInfo);
        rs.setTotal(cnt);

        return rs;
    }
    
    @Override
    public Optional<DBConnectionInfo> delete(DBConnectionInfoVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("DBConnectionInfoVo is required");
        });
        
        DBConnectionInfo connInfo = BeanUtils.copy(vo, DBConnectionInfo.class).get();
        testDBConnectionInfoMapper.deleteBy(connInfo);
        return Optional.of(connInfo);
    }

    @Override
    @Transactional
    public Integer delete(List<DBConnectionInfoVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<DBConnectionInfoVo> it = vos.iterator();
        while (it.hasNext()) {
            DBConnectionInfoVo vo = (DBConnectionInfoVo) it.next();
            delete(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<DBConnectionInfo> activate(DBConnectionInfoVo vo)
            throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiDeclarationVo is required");
        });
        
        DBConnectionInfo connInfo = BeanUtils.copy(vo, DBConnectionInfo.class).get();
        connInfo.setEnabled(true);
        testDBConnectionInfoMapper.stateSwitch(connInfo);
        
        return Optional.of(connInfo);
    }

    @Override
    @Transactional
    public Integer activate(List<DBConnectionInfoVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<DBConnectionInfoVo> it = vos.iterator();
        while (it.hasNext()) {
            DBConnectionInfoVo vo = (DBConnectionInfoVo) it.next();
            activate(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<DBConnectionInfo> deactivate(DBConnectionInfoVo vo)
            throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiDeclarationVo is required");
        });
        
        DBConnectionInfo connInfo = BeanUtils.copy(vo, DBConnectionInfo.class).get();
        connInfo.setEnabled(false);
        testDBConnectionInfoMapper.stateSwitch(connInfo);
        
        return Optional.of(connInfo);
    }

    @Override
    @Transactional
    public Integer deactivate(List<DBConnectionInfoVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<DBConnectionInfoVo> it = vos.iterator();
        while (it.hasNext()) {
            DBConnectionInfoVo vo = (DBConnectionInfoVo) it.next();
            deactivate(vo);
        }
        
        return vos.size();
    }

    @Override
    public Optional<DBConnectionInfo> updateById(DBConnectionInfoVo vo)
            throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("DBConnectionInfoVo is required");
        });
        Optional.ofNullable(vo.getId()).orElseThrow(() -> {
            throw new IllegalArgumentException("Id is required");
        });
        
        DBConnectionInfo connInfo = BeanUtils.copy(vo, DBConnectionInfo.class).get();
        connInfo.setModifiedBy(Consts.SYSTEM_ADMIN);
        testDBConnectionInfoMapper.updateById(connInfo);
        return Optional.of(connInfo);
    }

    private DBConnectionInfo convertToApiDeclaration(DBConnectionInfoVo vo) {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("vo is required");
        });
        
        try {
            Optional<DBConnectionInfo> rs = BeanUtils.copy(vo, DBConnectionInfo.class);
            return rs.get();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public DBConnection testDBConnection(DBConnectionInfoVo params) {
        DBConnection rs = new DBConnection();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(params.getUrl(), params.getUserName(), params.getPassword()); 
            Statement stsm = conn.createStatement();
            ResultSet rset = stsm.executeQuery("select * from helloWorld");
            while (rset.next()) {
                System.out.println(rset.getInt("id") + " " + rset.getString("name"));
            }
            rs.setStatus(true);
        } catch (ClassNotFoundException e) {
            rs.setStatus(false);
            rs.setErrorMsg(e.getMessage());
        } catch (SQLException e) {
            rs.setStatus(false);
            rs.setErrorMsg(e.getMessage());
        }
        
        return rs;
    }
}
