/**
 * 
 */
package org.iman.Heimdallr.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.entity.MindRawData;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.MindRawDataMapper;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.service.MindRawDataService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.MindRawDataVo;
import org.iman.Heimdallr.vo.NodeVo;
import org.iman.Heimdallr.vo.TestCaseVo;
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
public class MindRawDataServiceImpl implements MindRawDataService {

    private static final Logger log = LoggerFactory.getLogger(MindRawDataServiceImpl.class);

    @Autowired
    private MindRawDataMapper mindRawDataMapper;

    @Autowired
    private CaseGeneralInfoService caseGeneralInfoService;

    @Override
    public Optional<MindRawData> save(MindRawDataVo vo) throws DataConversionException {
        Optional<MindRawData> raw = null;
        raw = BeanUtils.copy(vo, MindRawData.class);

        if (raw.isEmpty()) {
            return Optional.empty();
        }
        MindRawData rawData = raw.get();
        mindRawDataMapper.insert(rawData);
        return Optional.ofNullable(rawData);
    }

    @Override
    public Optional<MindRawData> getByFunctionId(Long funcId) {
        Optional.ofNullable(funcId).orElseThrow(() -> {
            throw new IllegalArgumentException("FuncId is required");
        });

        MindRawData query = new MindRawData();
        query.setFunctionId(funcId);
        List<MindRawData> raws = mindRawDataMapper.selectBy(query);
        if (CollectionUtils.sizeIsEmpty(raws)) {
            return Optional.empty();
        }

        return Optional.of(raws.get(0));
    }

    @Override
    @Transactional
    public Integer generateTestCases(MindRawDataVo vo) throws DataConversionException {
        Optional<MindRawData> raw = save(vo);
        if (raw.isEmpty()) {
            return 0;
        }

        List<TestCaseVo> cases = generateTestCases0(vo, raw.get().getId());
        if (CollectionUtils.sizeIsEmpty(cases)) {
            return 0;
        }

        Integer rsCounts = caseGeneralInfoService.saveMultiCases(cases);
        return rsCounts;
    }

    private List<TestCaseVo> generateTestCases0(MindRawDataVo vo, Long rawId) {
        List<TestCaseVo> rs = new ArrayList<TestCaseVo>();
        Map<String, String> paths = new HashMap<String, String>();
        List<NodeVo> leafs = new ArrayList<NodeVo>();
        dfs(vo.getRawNode().getRoots().get(0), paths, "", leafs);

        if (CollectionUtils.sizeIsEmpty(leafs)) {
            return Collections.emptyList();
        }
        Iterator<NodeVo> it = leafs.iterator();
        while (it.hasNext()) {
            NodeVo node = (NodeVo) it.next();
            TestCaseVo caseVo = convertToTestCaseVo(paths.get(node.getId()), rawId, vo);
            rs.add(caseVo);
        }

        return rs;
    }

    private void dfs(NodeVo vo, Map<String, String> paths, String rootPath, List<NodeVo> leafs) {
//        System.out.println("NODE NAME: " + vo.getLabel() + " " + vo.getId());
        StringJoiner nodePath = new StringJoiner("|").add(rootPath).add(vo.getLabel());
        paths.put(vo.getId(), nodePath.toString());
        if (null == vo) {
            return;
        }

        if (!CollectionUtils.sizeIsEmpty(vo.getChildren())) {
            Iterator<NodeVo> it = vo.getChildren().iterator();
            while (it.hasNext()) {
                NodeVo nodeVo = (NodeVo) it.next();
                dfs(nodeVo, paths, nodePath.toString(), leafs);
            }
        } else {
            leafs.add(vo);
        }
    }

    private TestCaseVo convertToTestCaseVo(String path, Long rawId, MindRawDataVo rawDatavo) {
        TestCaseVo vo = new TestCaseVo();
        vo.setRawDataId(rawId);
        vo.setAppId(rawDatavo.getAppId());
        vo.setModuleId(rawDatavo.getModuleId());
        vo.setFunctionId(rawDatavo.getFunctionId());
        String[] paths = path.substring(1).split("\\|");
        vo.setResults(paths[paths.length - 3]);
        vo.setDescription(paths[paths.length - 1]);
        vo.setName(paths[paths.length - 2]);

        StringJoiner steps = new StringJoiner("\n");
        for (int i = 1; i < paths.length - 3; i++) {
            String step = (i + 1) + ". " + paths[i] + ";";
            steps.add(step);
        }

        vo.setStep(steps.toString());
        return vo;
    }

}
