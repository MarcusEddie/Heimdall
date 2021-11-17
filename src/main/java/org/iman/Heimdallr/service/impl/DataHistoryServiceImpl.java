package org.iman.Heimdallr.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.entity.DataHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.DataHistoryMapper;
import org.iman.Heimdallr.service.DataHistoryService;
import org.iman.Heimdallr.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class DataHistoryServiceImpl implements DataHistoryService {

    @Autowired
    private DataHistoryMapper dataHistoryMapper;

    @Override
    public DataHistory save(Object curObj, Object newObj, Long dataId, Action action, FuncTag tag,
            String opr) throws DataConversionException {
        DataHistory hist = new DataHistory(dataId, tag);
        hist.setActionType(action);
        switch (action) {
        case CREATE:
            JsonNode node = add(newObj);
            hist.setNewData(node);
            hist.setCreateBy(Consts.SYSTEM_ADMIN);
            hist.setCreateTime(LocalDateTime.now());
            break;
        case UPDATE:
            Pair<ObjectNode, ObjectNode> pair = diff(curObj, newObj);
            hist.setNewData(pair.getLeft());
            hist.setOldData(pair.getRight());
            hist.setCreateBy(Consts.SYSTEM_ADMIN);
            hist.setCreateTime(LocalDateTime.now());
            break;
        case DELETE:
            JsonNode curNode = add(curObj);
            hist.setOldData(curNode);
            hist.setCreateBy(Consts.SYSTEM_ADMIN);
            hist.setCreateTime(LocalDateTime.now());
            break;
            
        default:
            break;
        }
        
        dataHistoryMapper.insert(hist);
        return hist;
    }
    
    private ObjectNode add(Object newObj) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        ValueNode node2 = new ObjectMapper().createObjectNode().pojoNode(newObj);
        JsonObject object = new JsonParser().parse(node2.toPrettyString()).getAsJsonObject();

        Field[] fields = newObj.getClass().getDeclaredFields();
        List<String> fields2 = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().equals("serialVersionUID")) {
                fields2.add(fields[i].getName());
            }
        }
        Collections.sort(fields2);
        for (Iterator iterator = fields2.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            node.put(key, object.get(key).toString());
        }
        return node;
    }
    
    private Pair<ObjectNode, ObjectNode> diff(Object curObj, Object newObj) {
        ObjectNode newNodeFinal = new ObjectMapper().createObjectNode();
        ObjectNode curNodeFinal = new ObjectMapper().createObjectNode();
        ValueNode newNode = new ObjectMapper().createObjectNode().pojoNode(newObj);
        JsonObject newObject = new JsonParser().parse(newNode.toPrettyString()).getAsJsonObject();

        ValueNode curNode = new ObjectMapper().createObjectNode().pojoNode(curObj);
        JsonObject curObject = new JsonParser().parse(curNode.toPrettyString()).getAsJsonObject();
        
        Field[] fields = newObj.getClass().getDeclaredFields();
        List<String> fields2 = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().equals("serialVersionUID")) {
                fields2.add(fields[i].getName());
            }
        }
        Field[] superFields = newObj.getClass().getSuperclass().getDeclaredFields();
        for (int i = 0; i < superFields.length; i++) {
            if (superFields[i].getName().equals("enabled") || superFields[i].getName().equals("deleted")) {
                fields2.add(superFields[i].getName());
            }
        }
        Collections.sort(fields2);

        for (Iterator<String> iterator = fields2.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            String newVal = newObject.get(key).toString();
            String curVal = curObject.get(key).toString();
            if (StringUtils.isNotBlank(newVal) && newVal.equalsIgnoreCase("null")) {
                newVal = "";
            }
            if (StringUtils.isNotBlank(curVal) && curVal.equalsIgnoreCase("null")) {
                curVal = "";
            }
            
            if (StringUtils.isNotBlank(newVal) && StringUtils.isNotBlank(curVal)
                    && (!newVal.strip().equals(curVal.strip()))) {
                newNodeFinal.put(key, newVal.strip());
                curNodeFinal.put(key, curVal.strip());
            } else if (StringUtils.isNotBlank(newVal) && StringUtils.isBlank(curVal)) {
                newNodeFinal.put(key, newVal.strip());
                curNodeFinal.put(key, " ");
            }
        }

        return Pair.of(newNodeFinal, curNodeFinal);
    }
    
    public static void main(String[] args) {
        UiTestCase cas1 = new UiTestCase();
        cas1.setId(1L);
        cas1.setAppId(12L);
        ValueNode node2 = new ObjectMapper().createObjectNode().pojoNode(cas1);
//        System.out.println(node2);
//        {"enabled":true,"deleted":false,"createBy":"admin","createTime":modifiedBy":null,"modifiedTime":null,"id":1,"appId":12,"generalCaseId":null,"priority":null,"pageId":null,"name":null,"steps":null,"parameters":null,"expectedResult":null}
        JsonObject object = new JsonParser().parse(node2.toPrettyString()).getAsJsonObject();
        System.out.println(object.toString());
        System.out.println(object.get("id").toString());
    }
    
    @Override
    public Pagination<DataHistory> getLogsByDataIdAndFuncTag(Long id, FuncTag tag, Page page)
            throws DataConversionException {
        DataHistory req = new DataHistory(id, tag);
        List<DataHistory> logs = dataHistoryMapper.selectByPage(req, page.getOffset(),
                page.getCapacity());
        Pagination<DataHistory> rs = new Pagination<DataHistory>(page.getCurrent(),
                page.getPageSize());
        rs.setList(logs);
        
        int cnt = dataHistoryMapper.countByPage(req);
        rs.setTotal(cnt);
        
        return rs;
    }

    @Override
    public List<DataHistory> getLogsByDataIdAndFuncTag(Long id, FuncTag tag)
            throws DataConversionException {
        DataHistory req = new DataHistory(id, tag);
        List<DataHistory> logs = dataHistoryMapper.selectBy(req);
        
        return logs;
    }



}
