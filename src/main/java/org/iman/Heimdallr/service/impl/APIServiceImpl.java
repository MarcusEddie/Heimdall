/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ApiDeclarationMapper;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.Pagination;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ey
 *
 */
@Service
public class APIServiceImpl implements APIService {

    private static final Logger log = LoggerFactory.getLogger(APIServiceImpl.class);

    @Autowired
    private ApiDeclarationMapper apiDeclarationMapper;

    @Override
    public ApiDeclaration saveOneAPI(ApiDeclarationVo vo) {
        ApiDeclaration declaration = convertToApiDeclaration(vo);
        apiDeclarationMapper.insert(declaration);
        return declaration;
    }

    @Override
    public List<ApiDeclaration> getByParams(ApiDeclarationVo criteria) {
        ApiDeclaration declaration = convertToApiDeclaration(criteria);
        List<ApiDeclaration> rs = apiDeclarationMapper.selectBy(declaration);
        return rs;
    }

    @Override
    public Pagination<ApiDeclaration> getByParams(ApiDeclarationVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });
        
        Pagination<ApiDeclaration> rs = new Pagination<ApiDeclaration>(page.getCurrent(),
                page.getPageSize());
        
        ApiDeclaration declaration = convertToApiDeclaration(criteria);
        declaration.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<ApiDeclaration> apis = apiDeclarationMapper.selectByPage(declaration, page.getOffset(),
                page.getCapacity());
        rs.setList(apis);
        int cnt = apiDeclarationMapper.countByPage(declaration);
        rs.setTotal(cnt);
        
        return rs;
    }
    
    @Override
    public Optional<ApiDeclaration> getById(Long id) {
        ApiDeclarationVo criteria = new ApiDeclarationVo(id);
        List<ApiDeclaration> rs = getByParams(criteria);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }
        
        return Optional.of(rs.get(0));
    }

    @Override
    public Optional<ApiDeclaration> delete(ApiDeclarationVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiDeclarationVo is required");
        });
        
        ApiDeclaration declaration = BeanUtils.copy(vo, ApiDeclaration.class).get();
        apiDeclarationMapper.deleteBy(declaration);
        return Optional.of(declaration);
    }

    @Override
    @Transactional
    public Integer delete(List<ApiDeclarationVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<ApiDeclarationVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiDeclarationVo vo = (ApiDeclarationVo) it.next();
            delete(vo);
        }
        return vos.size();
    }

    @Override
    public Optional<ApiDeclaration> activate(ApiDeclarationVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiDeclarationVo is required");
        });
        
        ApiDeclaration declaration = BeanUtils.copy(vo, ApiDeclaration.class).get();
        declaration.setEnabled(true);
        apiDeclarationMapper.stateSwitch(declaration);
        return Optional.of(declaration);
    }

    @Override
    @Transactional
    public Integer activate(List<ApiDeclarationVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<ApiDeclarationVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiDeclarationVo vo = (ApiDeclarationVo) it.next();
            activate(vo);
        }
        return vos.size();
    }

    @Override
    public Optional<ApiDeclaration> deactivate(ApiDeclarationVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ApiDeclarationVo is required");
        });
        
        ApiDeclaration declaration = BeanUtils.copy(vo, ApiDeclaration.class).get();
        declaration.setEnabled(false);
        apiDeclarationMapper.stateSwitch(declaration);
        return Optional.of(declaration);
    }

    @Override
    public Integer deactivate(List<ApiDeclarationVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<ApiDeclarationVo> it = vos.iterator();
        while (it.hasNext()) {
            ApiDeclarationVo vo = (ApiDeclarationVo) it.next();
            deactivate(vo);
        }
        return vos.size();
    }
    
    @Override
    public Optional<ApiDeclaration> updateById(ApiDeclarationVo vo) throws DataConversionException {
        Optional.ofNullable(vo.getId()).orElseThrow(() -> {
            throw new IllegalArgumentException("Id is required");
        });
        
        ApiDeclaration declaration = convertToApiDeclaration(vo);
        System.out.println("UPDATE BY ID SERVICE ===> " + declaration.toString());
        declaration.setModifiedBy(Consts.SYSTEM_ADMIN);
        apiDeclarationMapper.updateById(declaration);
        
        return Optional.of(declaration);
    }
    
    private ApiDeclaration convertToApiDeclaration(ApiDeclarationVo vo) {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("vo is required");
        });
        
        try {
            Optional<ApiDeclaration> rs = BeanUtils.copy(vo, ApiDeclaration.class);
            return rs.get();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
