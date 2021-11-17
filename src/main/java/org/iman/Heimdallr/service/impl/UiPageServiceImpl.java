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
import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.UiPageMapper;
import org.iman.Heimdallr.service.UiPageService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.UiPageVo;
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
public class UiPageServiceImpl implements UiPageService {

    private static final Logger log = LoggerFactory.getLogger(UiPageServiceImpl.class);

    @Autowired
    private UiPageMapper uiPageMapper;

    @Override
    public UiPage saveOnePage(UiPageVo vo) {
        UiPage page = convertToUiPage(vo);
        uiPageMapper.insert(page);
        return page;
    }

    @Override
    public List<UiPage> getByParams(UiPageVo criteria) {
        UiPage page = convertToUiPage(criteria);
        List<UiPage> rs = uiPageMapper.selectBy(page);
        return rs;
    }

    @Override
    public Pagination<UiPage> getByParams(UiPageVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });
        
        Pagination<UiPage> rs = new Pagination<UiPage>(page.getCurrent(),
                page.getPageSize());
        
        UiPage uiPage = convertToUiPage(criteria);
        uiPage.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<UiPage> pgs = uiPageMapper.selectByPage(uiPage, page.getOffset(), page.getCapacity());
        rs.setList(pgs);
        int cnt = uiPageMapper.countByPage(uiPage);
        rs.setTotal(cnt);
        
        return rs;
    }
    
    @Override
    public Optional<UiPage> getById(Long id) {
        UiPageVo criteria = new UiPageVo(id);
        List<UiPage> rs = getByParams(criteria);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }
        
        return Optional.of(rs.get(0));
    }

    @Override
    public Optional<UiPage> delete(UiPageVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiPageVo is required");
        });
        
        UiPage page = BeanUtils.copy(vo, UiPage.class).get();
        uiPageMapper.deleteBy(page);
        return Optional.of(page);
    }

    @Override
    @Transactional
    public Integer delete(List<UiPageVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<UiPageVo> it = vos.iterator();
        while (it.hasNext()) {
            UiPageVo vo = (UiPageVo) it.next();
            delete(vo);
        }
        return vos.size();
    }

    @Override
    public Optional<UiPage> activate(UiPageVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiPageVo is required");
        });
        
        UiPage page = BeanUtils.copy(vo, UiPage.class).get();
        page.setEnabled(true);
        uiPageMapper.stateSwitch(page);
        return Optional.of(page);
    }

    @Override
    @Transactional
    public Integer activate(List<UiPageVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        
        Iterator<UiPageVo> it = vos.iterator();
        while (it.hasNext()) {
            UiPageVo vo = (UiPageVo) it.next();
            activate(vo);
        }
        return vos.size();
    }

    @Override
    public Optional<UiPage> deactivate(UiPageVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("UiPageVo is required");
        });
        
        UiPage page = BeanUtils.copy(vo, UiPage.class).get();
        page.setEnabled(false);
        uiPageMapper.stateSwitch(page);
        return Optional.of(page);
    }

    @Override
    public Integer deactivate(List<UiPageVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }
        Iterator<UiPageVo> it = vos.iterator();
        while (it.hasNext()) {
            UiPageVo vo = (UiPageVo) it.next();
            deactivate(vo);
        }
        return vos.size();
    }
    
    @Override
    public Optional<UiPage> updateById(UiPageVo vo) throws DataConversionException {
        Optional.ofNullable(vo.getId()).orElseThrow(() -> {
            throw new IllegalArgumentException("Id is required");
        });
        
        UiPage page = convertToUiPage(vo);
        System.out.println("UPDATE BY ID SERVICE ===> " + page.toString());
        page.setModifiedBy(Consts.SYSTEM_ADMIN);
        uiPageMapper.updateById(page);
        
        return Optional.of(page);
    }
    
    private UiPage convertToUiPage(UiPageVo vo) {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("vo is required");
        });
        
        try {
            Optional<UiPage> rs = BeanUtils.copy(vo, UiPage.class);
            return rs.get();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
