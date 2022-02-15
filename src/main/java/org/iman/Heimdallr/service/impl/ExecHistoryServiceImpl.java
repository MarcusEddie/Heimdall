package org.iman.Heimdallr.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.ExecHistoryMapper;
import org.iman.Heimdallr.service.DataHistoryService;
import org.iman.Heimdallr.service.ExecHistoryService;
import org.iman.Heimdallr.service.ExecHistoryFailureDetailsService;
import org.iman.Heimdallr.service.TaskQueueService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ExecHistoryFailureDetailsVo;
import org.iman.Heimdallr.vo.ExecHistoryVo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TaskQueueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ExecHistoryServiceImpl implements ExecHistoryService {

    @Autowired
    private ExecHistoryMapper execHistoryMapper;
    @Autowired
    private DataHistoryService dataHistoryService;
    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private ExecHistoryFailureDetailsService execHistoryFailureDetailsService;

    @Override
    @Transactional
    public ExecHistory save(ExecHistoryVo historyVo, Map<String, List<String>> errors)
            throws DataConversionException {

        ExecHistory history = BeanUtils.copy(historyVo, ExecHistory.class).get();
        Integer cnt = execHistoryMapper.insert(history);

        if (historyVo.getTaskState().equals(TaskState.FAILED)) {
            Iterator<Entry<String, List<String>>> it = errors.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, List<String>> entry = (Entry<String, List<String>>) it.next();
                ExecHistoryFailureDetailsVo vo = new ExecHistoryFailureDetailsVo();
                vo.setHistoryId(history.getId());
                vo.setCaseName(entry.getKey());
                List<String> errList = entry.getValue();
                ObjectNode errNode = new ObjectMapper().createObjectNode();
                for (int i = 0; i < errList.size(); i++) {
                    errNode.put(String.valueOf(i + 1), errList.get(i));
                }
                vo.setCaseError(errNode);
                execHistoryFailureDetailsService.save(vo);
            }
        }
        dataHistoryService.save(null, history, history.getId(), Action.CREATE, FuncTag.EXEC_HISTORY,
                Consts.SYSTEM_ADMIN);

        return history;
    }

    @Override
    public Optional<ExecHistory> getById(Long id) throws DataConversionException {
        Optional.ofNullable(id).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        ExecHistory criteria = new ExecHistory(id);
        List<ExecHistory> histories = execHistoryMapper.selectById(criteria);
        if (CollectionUtils.sizeIsEmpty(histories)) {
            return Optional.empty();
        }

        return Optional.of(histories.get(0));
    }

    @Override
    public Pagination<ExecHistory> getByParams(ExecHistoryVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("ExecHistoryVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<ExecHistory> rs = new Pagination<ExecHistory>(page.getCurrent(),
                page.getPageSize());
        ExecHistory history = BeanUtils.copy(criteria, ExecHistory.class).get();
        history.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));

        List<ExecHistory> histories = execHistoryMapper.selectByPage(history, page.getOffset(),
                page.getCapacity());
        rs.setList(histories);
        Integer cnt = execHistoryMapper.countByPage(history);
        rs.setTotal(cnt);

        return rs;
    }

    @Override
    @Transactional
    public Optional<TaskQueue> reEnqueue(ExecHistoryVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("ExecHistoryVo is required");
        });

        ExecHistory history = BeanUtils.copy(vo, ExecHistory.class).get();
        Optional<ExecHistory> rs = getById(history.getId());
        dataHistoryService.save(rs.get(), null, rs.get().getId(), Action.DELETE,
                FuncTag.EXEC_HISTORY, Consts.SYSTEM_ADMIN);

        execHistoryMapper.deleteBy(history);
        TaskQueue queue = taskQueueService.save(convertToQueueVo(rs.get()));
        return Optional.of(queue);
    }

    private TaskQueueVo convertToQueueVo(ExecHistory history) {
        TaskQueueVo historyVo = new TaskQueueVo();
        historyVo.setPlanId(history.getPlanId());
        historyVo.setPlanName(history.getPlanName());
        historyVo.setType(TaskType.FROM_HIST);
        historyVo.setTaskState(TaskState.DELAYED);
        historyVo.setTestType(history.getTestType());
        historyVo.setPriority(history.getPriority());

        return historyVo;
    }
}
