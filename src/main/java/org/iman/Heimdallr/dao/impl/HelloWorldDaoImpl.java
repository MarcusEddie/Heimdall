package org.iman.Heimdallr.dao.impl;

import java.util.List;
import java.util.Map;

import org.iman.Heimdallr.dao.HelloWorldDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HelloWorldDaoImpl implements HelloWorldDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public String fetchById(Long id) {
        String sql = "select name from hello_world where id = 1";
        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
        return rs.get(0).toString();
    }
}
