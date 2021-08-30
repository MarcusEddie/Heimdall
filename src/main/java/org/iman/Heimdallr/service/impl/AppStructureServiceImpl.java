/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.iman.Heimdallr.constants.enums.AppLevel;
import org.iman.Heimdallr.entity.App;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.Function;
import org.iman.Heimdallr.entity.Module;
import org.iman.Heimdallr.mapper.AppStructureMapper;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.vo.FunctionVo;
import org.iman.Heimdallr.vo.ModuleVo;
import org.iman.Heimdallr.vo.AppVo;
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
public class AppStructureServiceImpl implements AppStructureService {

    private static final Logger log = LoggerFactory.getLogger(AppStructureServiceImpl.class);
    @Autowired
    private AppStructureMapper appStructureMapper;

    @Override
    @Transactional
    public AppStructure saveApp(AppVo obj) {
        AppStructure app = convertToAppStructure(obj.getName(), null, AppLevel.APP);
        Optional<AppStructure> queryRs = getStructures(app.getName(), AppLevel.APP, 0L);
        if (queryRs.isEmpty()) {
            appStructureMapper.insert(app);
        }
        if (CollectionUtils.sizeIsEmpty(obj.getModules())) {
            return app;
        }

        Iterator<ModuleVo> it = obj.getModules().iterator();
        while (it.hasNext()) {
            ModuleVo vo = (ModuleVo) it.next();
            vo.setRoot(app.getId());
            saveModule(vo);

        }
        return app;
    }

    @Override
    @Transactional
    public AppStructure saveModule(ModuleVo obj) {
        AppStructure module = convertToAppStructure(obj.getName(), obj.getRoot(), AppLevel.MODULE);
        Optional<AppStructure> queryRs = getStructures(module.getName(), AppLevel.MODULE,
                module.getRoot());
        if (queryRs.isEmpty()) {
            appStructureMapper.insert(module);
        }
        if (CollectionUtils.sizeIsEmpty(obj.getFunctions())) {
            return module;
        }

        Iterator<FunctionVo> it = obj.getFunctions().iterator();
        while (it.hasNext()) {
            FunctionVo vo = (FunctionVo) it.next();
            vo.setRoot(module.getId());
            saveFunction(vo);
        }
        return module;
    }

    @Override
    public AppStructure saveFunction(FunctionVo obj) {
        AppStructure func = convertToAppStructure(obj.getName(), obj.getRoot(), AppLevel.FUNCTION);
        Optional<AppStructure> queryRs = getStructures(func.getName(), AppLevel.FUNCTION,
                func.getRoot());
        if (queryRs.isEmpty()) {
            appStructureMapper.insert(func);
        }

        return func;
    }

    @Override
    public List<AppStructure> getStructures(AppLevel level, Long root, Boolean lexicographicOrder) {
        AppStructure query = new AppStructure();
        query.setLevel(level.getLevel());
        query.setRoot(root);

        List<AppStructure> rs = appStructureMapper.selectBy(query);
        if (lexicographicOrder) {
            Collections.sort(rs);
        }
        return rs;
    }

    @Override
    public List<AppStructure> getStructures(AppLevel level, Long root) {
        return getStructures(level, root, true);
    }

    @Override
    public Optional<AppStructure> getStructures(String name, AppLevel level, Long root) {
        AppStructure query = new AppStructure();
        query.setName(StringUtils.isNotBlank(name.strip()) ? name.strip() : null);
        query.setLevel(level.getLevel());
        query.setRoot(root);
        List<AppStructure> rs = appStructureMapper.selectBy(query);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }

        return Optional.of(rs.get(0));
    }

    @Override
    public Optional<AppStructure> getById(Long id) {
        AppStructure query = new AppStructure(id);
        List<AppStructure> rs = appStructureMapper.selectBy(query);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }

        return Optional.of(rs.get(0));
    }

    @Override
    @Transactional
    public App saveComponentTree(String appVal, String moduleVal, String functionVal) {
        if (!StringUtils.isNoneBlank(appVal.strip(), moduleVal.strip(), functionVal.strip())) {
            throw new IllegalArgumentException("appVal, moduleVal and functionVal is required");
        }
        appVal = appVal.strip();
        moduleVal = moduleVal.strip();
        functionVal = functionVal.strip();

        Long appId = 0L, moduleId = 0L;
        try {
            appId = Long.valueOf(appVal);
            Optional<AppStructure> app = getById(appId);
            if (app.isPresent()) {
                appVal = app.get().getName();
            }
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("Insert new app with name {}", appVal);
            }
            AppVo vo = new AppVo();
            vo.setName(appVal);
            AppStructure app = saveApp(vo);
            appId = app.getId();
        }

        App rs = new App(appId, appVal);
        rs.setModules(new ArrayList<Module>());

        try {
            moduleId = Long.valueOf(moduleVal);
            Optional<AppStructure> module = getById(moduleId);
            if (module.isPresent()) {
                moduleVal = module.get().getName();
            }
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("Insert new module with name {}", moduleVal);
            }
            ModuleVo vo = new ModuleVo();
            vo.setName(moduleVal);
            vo.setRoot(appId);
            AppStructure module = saveModule(vo);
            moduleId = module.getId();
        }

        Module module = new Module(moduleId, moduleVal);
        module.setRoot(appId);
        module.setFunctions(new ArrayList<Function>());

        FunctionVo vo = new FunctionVo();
        vo.setName(functionVal);
        vo.setRoot(moduleId);
        AppStructure function = saveFunction(vo);
        Function func = new Function(function.getId(), function.getName());
        func.setRoot(function.getRoot());
        module.getFunctions().add(func);
        rs.getModules().add(module);
        return rs;
    }

    private AppStructure convertToAppStructure(String name, Long root, AppLevel level) {
        if (StringUtils.isBlank(name.strip())) {
            throw new IllegalArgumentException("Name is required and can not be null or empty");
        }
        AppStructure rs = new AppStructure();
        rs.setName(name.strip());
        rs.setRoot((null != root && root.compareTo(0L) == 1) ? root : 0L);
        rs.setLevel(level.getLevel());
        return rs;
    }

    @Override
    @Transactional
    public Integer deleteComponent(Long appId, Long moduleId, Long functionId) {
        Optional.ofNullable(appId).orElseThrow(() -> {
            throw new IllegalArgumentException("AppId is required");
        });
        Optional.ofNullable(moduleId).orElseThrow(() -> {
            throw new IllegalArgumentException("ModuleId is required");
        });
        Optional.ofNullable(functionId).orElseThrow(() -> {
            throw new IllegalArgumentException("FunctionId is required");
        });

        AppStructure criteria = new AppStructure();
        Integer cnt = 0;
        if (appId.compareTo(0L) == 1 && moduleId.compareTo(0L) == 1 && functionId.compareTo(0L) == 1) {
            // only delete function id
            criteria.setId(functionId);
            cnt = appStructureMapper.deleteBy(criteria);
        } else if (appId.compareTo(0L) == 1 && moduleId.compareTo(0L) == 1 && functionId.compareTo(0L) == 0) {
            // delete all function ids and moduel id
            criteria.setRoot(moduleId);
            cnt = appStructureMapper.deleteBy(criteria);
            criteria.setRoot(null);
            criteria.setId(moduleId);
            cnt = cnt + appStructureMapper.deleteBy(criteria);
        } else if (appId.compareTo(0L) == 1 && functionId.compareTo(0L) == 0 && moduleId.compareTo(0L) == 0) {
            // delete all function ids, all module ids, and app id
            List<AppStructure> modules = getStructures(AppLevel.MODULE, appId, false);
            if (!CollectionUtils.sizeIsEmpty(modules)) {
                AppStructure criteria2 = new AppStructure();
                Iterator<AppStructure> it = modules.iterator();
                while (it.hasNext()) {
                    criteria2.setId(null);
                    AppStructure appStructure = (AppStructure) it.next();
                    criteria2.setRoot(appStructure.getId());
                    cnt = cnt + appStructureMapper.deleteBy(criteria2);
                    criteria2.setRoot(null);
                    criteria2.setId(appStructure.getId());
                    cnt = cnt + appStructureMapper.deleteBy(criteria2);
                    
                }
            }
            criteria.setId(appId);
            cnt = cnt + appStructureMapper.deleteBy(criteria);
        }
        
        return cnt;
    }

}
