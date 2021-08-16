/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import javax.annotation.Resource;

import org.iman.Heimdallr.dao.HelloWorldDao;
import org.iman.Heimdallr.service.HelloWordService;
import org.springframework.stereotype.Service;

/**
 * @author ey
 *
 */
@Service
public class HelloWordServiceImpl implements HelloWordService{

    @Resource
    private HelloWorldDao helloWorldDao;
    
    @Override
    public String getById(Long id) {
        return helloWorldDao.fetchById(id);
    }

}
