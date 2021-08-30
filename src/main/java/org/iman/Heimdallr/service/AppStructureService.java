/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.List;
import java.util.Optional;

import org.iman.Heimdallr.constants.enums.AppLevel;
import org.iman.Heimdallr.entity.App;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.vo.FunctionVo;
import org.iman.Heimdallr.vo.ModuleVo;
import org.iman.Heimdallr.vo.AppVo;

/**
 * @author ey
 *
 */
public interface AppStructureService {

    public AppStructure saveApp(AppVo obj);
    
    public AppStructure saveModule(ModuleVo obj);
    
    public AppStructure saveFunction(FunctionVo obj);
    
    public Optional<AppStructure> getById(Long id);
    
    public List<AppStructure> getStructures(AppLevel level, Long root, Boolean lexicographicOrder);
    public List<AppStructure> getStructures(AppLevel level, Long root);
    
    public Optional<AppStructure> getStructures(String name, AppLevel level, Long root);
    
    public App saveComponentTree(String appVal, String moduleVal, String functionVal);
    
    public Integer deleteComponent(Long appId, Long moduleId, Long functionId);
}
