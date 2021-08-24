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
import org.iman.Heimdallr.constant.AppLevel;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.Function;
import org.iman.Heimdallr.entity.Module;
import org.iman.Heimdallr.entity.System;
import org.iman.Heimdallr.mapper.AppStructureMapper;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.vo.FunctionVo;
import org.iman.Heimdallr.vo.ModuleVo;
import org.iman.Heimdallr.vo.SystemVo;
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
    private AppStructureMapper appStructure;

    @Override
    @Transactional
    public AppStructure saveSystem(SystemVo obj) {
        AppStructure system = convertToAppStructure(obj.getName(), null, AppLevel.SYSTEM);
        Optional<AppStructure> queryRs = getStructures(system.getName(), AppLevel.SYSTEM, 0L);
        if (queryRs.isEmpty()) {
            appStructure.insert(system);
        }
        if (CollectionUtils.sizeIsEmpty(obj.getModules())) {
            return system;
        }

        Iterator<ModuleVo> it = obj.getModules().iterator();
        while (it.hasNext()) {
            ModuleVo vo = (ModuleVo) it.next();
            vo.setRoot(system.getId());
            saveModule(vo);

        }
        return system;
    }

    @Override
    @Transactional
    public AppStructure saveModule(ModuleVo obj) {
        AppStructure module = convertToAppStructure(obj.getName(), obj.getRoot(), AppLevel.MODULE);
        Optional<AppStructure> queryRs = getStructures(module.getName(), AppLevel.MODULE,
                module.getRoot());
        if (queryRs.isEmpty()) {
            appStructure.insert(module);
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
            appStructure.insert(func);
        }

        return func;
    }

    @Override
    public List<AppStructure> getStructures(AppLevel level, Long root, Boolean lexicographicOrder) {
        AppStructure query = new AppStructure();
        query.setLevel(level.getLevel());
        query.setRoot(root);

        List<AppStructure> rs = appStructure.selectBy(query);
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
        List<AppStructure> rs = appStructure.selectBy(query);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }

        return Optional.of(rs.get(0));
    }

    @Override
    public Optional<AppStructure> getById(Long id) {
        AppStructure query = new AppStructure(id);
        List<AppStructure> rs = appStructure.selectBy(query);
        if (CollectionUtils.sizeIsEmpty(rs)) {
            return Optional.empty();
        }

        return Optional.of(rs.get(0));
    }

    @Override
    @Transactional
    public System saveComponentTree(String systemVal, String moduleVal, String functionVal) {
        if (!StringUtils.isNoneBlank(systemVal.strip(), moduleVal.strip(), functionVal.strip())) {
            throw new IllegalArgumentException("System, module and function is required");
        }
        systemVal = systemVal.strip();
        moduleVal = moduleVal.strip();
        functionVal = functionVal.strip();
        
        Long systemId = 0L, moduleId = 0L;
        try {
            systemId = Long.valueOf(systemVal);
            Optional<AppStructure> system = getById(systemId);
            if (system.isPresent()) {
                systemVal = system.get().getName();
            }
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("Insert new system with name {}", systemVal);
            }
            SystemVo vo = new SystemVo();
            vo.setName(systemVal);
            AppStructure system = saveSystem(vo);
            systemId = system.getId();
        }
        
        System rs = new System(systemId, systemVal);
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
            vo.setRoot(systemId);
            AppStructure module = saveModule(vo);
            moduleId = module.getId();
        }

        Module module = new Module(moduleId, moduleVal);
        module.setRoot(systemId);
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
    
}
