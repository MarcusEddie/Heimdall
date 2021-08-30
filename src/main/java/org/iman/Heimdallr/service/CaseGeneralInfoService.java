/**
 * 
 */
package org.iman.Heimdallr.service;

import org.iman.Heimdallr.DataNotFoundException;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.vo.TestCaseVo;

/**
 * @author ey
 *
 */
public interface CaseGeneralInfoService {

    public TestCase saveOneCase(TestCaseVo vo)  throws DataNotFoundException;
}
