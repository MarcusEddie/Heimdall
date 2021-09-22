/**
 * 
 */
package org.iman.Heimdallr.service;

import java.util.Optional;

import org.iman.Heimdallr.entity.MindRawData;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.vo.MindRawDataVo;

/**
 * @author ey
 *
 */
public interface MindRawDataService {

    public Integer generateTestCases(MindRawDataVo vo) throws DataConversionException;

    public Optional<MindRawData> save(MindRawDataVo vo) throws DataConversionException;
    
    public Optional<MindRawData> getByFunctionId(Long funcId);
}
