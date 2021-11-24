package org.iman.Heimdallr.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.constants.enums.TestType;
import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.mapper.TaskQueueMapper;
import org.iman.Heimdallr.service.DataHistoryService;
import org.iman.Heimdallr.service.ExecHistoryService;
import org.iman.Heimdallr.service.TaskQueueService;
import org.iman.Heimdallr.service.TestPlanService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.vo.ExecHistoryVo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TaskQueueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TaskQueueServiceImpl implements TaskQueueService {

    @Autowired
    private TaskQueueMapper taskQueueMapper;
    @Autowired
    private ExecHistoryService execHistoryService;
    @Autowired
    private DataHistoryService dataHistoryService;
    @Autowired
    private TestPlanService testPlanService;

    @Override
    @Transactional
    public Optional<ExecHistory> delete(TaskQueueVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TaskQueueVo is required");
        });

        TaskQueue task = BeanUtils.copy(vo, TaskQueue.class).get();
        Optional<TaskQueue> rs = getById(task.getId());
        dataHistoryService.save(rs.get(), null, rs.get().getId(), Action.DELETE, FuncTag.TASK_QUEUE,
                Consts.SYSTEM_ADMIN);

        TestPlan plan = testPlanService.getById(rs.get().getPlanId()).get();
        ObjectNode details = genExecDetails(plan.getCaseSize(), 0, 0, 0, 0);
        ExecHistoryVo historyVo = convertToHistoryVo(rs.get());
        historyVo.setDetails(details);

        taskQueueMapper.deleteBy(task);

        ExecHistory history = execHistoryService.save(historyVo);
        return Optional.of(history);
    }

    @Override
    public Integer delete(List<TaskQueueVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TaskQueueVo> it = vos.iterator();
        while (it.hasNext()) {
            TaskQueueVo vo = (TaskQueueVo) it.next();
            delete(vo);
        }

        return vos.size();
    }

    @Override
    public Optional<TaskQueue> activate(TaskQueueVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TaskQueueVo is required");
        });

        TaskQueue plan = BeanUtils.copy(vo, TaskQueue.class).get();
        plan.setEnabled(true);
        plan.setTaskState(TaskState.READY);
        Optional<TaskQueue> oldObj = getById(plan.getId());
        dataHistoryService.save(oldObj.get(), plan, plan.getId(), Action.UPDATE, FuncTag.TASK_QUEUE,
                Consts.SYSTEM_ADMIN);

        taskQueueMapper.stateSwitch(plan);

        return Optional.of(plan);
    }

    @Override
    public Integer activate(List<TaskQueueVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TaskQueueVo> it = vos.iterator();
        while (it.hasNext()) {
            TaskQueueVo vo = (TaskQueueVo) it.next();
            activate(vo);
        }

        return vos.size();
    }

    @Override
    public Optional<TaskQueue> deactivate(TaskQueueVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TestPlanVo is required");
        });

        TaskQueue plan = BeanUtils.copy(vo, TaskQueue.class).get();
        plan.setEnabled(false);
        plan.setTaskState(TaskState.CANCELD);

        Optional<TaskQueue> oldObj = getById(plan.getId());
        dataHistoryService.save(oldObj.get(), plan, plan.getId(), Action.UPDATE, FuncTag.TASK_QUEUE,
                Consts.SYSTEM_ADMIN);

        taskQueueMapper.stateSwitch(plan);
        return Optional.of(plan);
    }

    @Override
    public Integer deactivate(List<TaskQueueVo> vos) throws DataConversionException {
        if (CollectionUtils.sizeIsEmpty(vos)) {
            return 0;
        }

        Iterator<TaskQueueVo> it = vos.iterator();
        while (it.hasNext()) {
            TaskQueueVo vo = (TaskQueueVo) it.next();
            deactivate(vo);
        }

        return vos.size();
    }

    @Override
    public TaskQueue save(TaskQueueVo vo) throws DataConversionException {
        Optional.ofNullable(vo).orElseThrow(() -> {
            throw new IllegalArgumentException("TaskQueueVo is required");
        });

        Optional<TaskQueue> cpObj = BeanUtils.copy(vo, TaskQueue.class);
        TaskQueue queue = cpObj.get();

        if (null == queue.getTriggerTime()) {
            TestPlan plan = testPlanService.getById(queue.getPlanId()).get();
            queue.setTriggerTime(plan.getTriggerTime());
        }

        try {
            queue.setProgress(0);
            enqueue(queue);
            dataHistoryService.save(null, queue, queue.getId(), Action.CREATE,
                    FuncTag.UI_TEST_CASE_DETAILS, Consts.SYSTEM_ADMIN);
        } catch (SQLIntegrityConstraintViolationException | DuplicateKeyException e) {
        }

        return queue;
    }

    @Override
    public Pagination<TaskQueue> getByParams(TaskQueueVo criteria, Page page)
            throws DataConversionException {
        Optional.ofNullable(criteria).orElseThrow(() -> {
            throw new IllegalArgumentException("TaskQueueVo is required");
        });
        Optional.ofNullable(page).orElseThrow(() -> {
            throw new IllegalArgumentException("page is required");
        });

        Pagination<TaskQueue> rs = new Pagination<TaskQueue>(page.getCurrent(), page.getPageSize());
        TaskQueue plan = BeanUtils.copy(criteria, TaskQueue.class).get();
        plan.setEnabled(TestCaseState.convertToBoolean(criteria.getState()));
        List<TaskQueue> plans = taskQueueMapper.selectInQueues(plan, page.getOffset(),
                page.getCapacity());
        rs.setList(plans);
        Integer cnt = taskQueueMapper.countInQueues(plan);
        rs.setTotal(cnt);

        return rs;
    }

    private void enqueue(TaskQueue queue)
            throws SQLIntegrityConstraintViolationException, DuplicateKeyException {
        taskQueueMapper.insert(queue);
    }

    private ExecHistoryVo convertToHistoryVo(TaskQueue queue) {
        ExecHistoryVo historyVo = new ExecHistoryVo();
        historyVo.setPlanId(queue.getPlanId());
        historyVo.setPlanName(queue.getPlanName());
        historyVo.setType(queue.getType());
        historyVo.setTaskState(TaskState.DELETED);
        historyVo.setTriggerTime(null);
        historyVo.setPriority(queue.getPriority());
        historyVo.setTestType(queue.getTestType());
        return historyVo;
    }

    private ObjectNode genExecDetails(Integer totalNum, Integer totalSuccessNum,
            Integer totalFailNum, Integer nonExecNum, Integer min) {
        ObjectNode details = new ObjectMapper().createObjectNode();
        details.put("Total number of cases", totalNum);
        details.put("Total success number of cases", totalSuccessNum);
        details.put("Total failed number of cases", totalFailNum);
        details.put("Total nonexecution number of cases", nonExecNum);
        details.put("Total minutes has ran", min);
        return details;
    }

    @Override
    public Optional<TaskQueue> getById(Long id) throws DataConversionException {
        Optional.ofNullable(id).orElseThrow(() -> {
            throw new IllegalArgumentException("criteria is required");
        });
        UiTestCase criteria = new UiTestCase(id);
        TaskQueue plan = taskQueueMapper.selectById(criteria);
        if (null == plan || null != plan && null == plan.getId()) {
            return Optional.empty();
        }

        return Optional.of(plan);
    }

    @Override
    public Optional<TaskQueue> requestATask(TestType testType) throws DataConversionException {
        LocalDateTime end = LocalDateTime.now().plusSeconds(5L);
        TaskQueue criteria = new TaskQueue();
        criteria.setTestType(testType);
        criteria.setTaskState(TaskState.READY);
        criteria.setTriggerTime(end);
        TaskQueue task = taskQueueMapper.pickOneReadyTask(criteria);
        if (null != task && null != task.getId()) {
            task.setTaskState(TaskState.RUNNING);
//            taskQueueMapper.updateById(task);
            return Optional.of(task);
        }
        criteria.setTaskState(TaskState.DELAYED);
        criteria.setTriggerTime(null);
        TaskQueue delayTask = taskQueueMapper.pickOneDelayTask(criteria);
        if (null != delayTask && null != delayTask.getId()) {
            delayTask.setTaskState(TaskState.RUNNING);
//            taskQueueMapper.updateById(delayTask);
            return Optional.of(delayTask);
        }
        
        return Optional.empty();
    }

}
