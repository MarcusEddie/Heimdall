/**
 * 
 */
package org.iman.Heimdallr.scheduling;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.mapper.TaskQueueMapper;
import org.iman.Heimdallr.mapper.TestPlanMapper;
import org.iman.Heimdallr.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ey
 *
 */
@Component
public class PlanScheduler {

    @Autowired
    private TestPlanMapper testPlanMapper;
    @Autowired
    private TaskQueueMapper taskQueueMapper;

    @Scheduled(fixedRate = 6000)
    public void scheduling() {
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        TestPlan criteria = new TestPlan();
        criteria.setTriggerTime(end);
        criteria.setRepeatFlag(false);
        criteria.setEnabled(true);
        List<TestPlan> nonRepeatplans = testPlanMapper.selectNonRepeatPlanBeforeEndTime(criteria);
        if (!CollectionUtils.sizeIsEmpty(nonRepeatplans)) {
            Iterator<TestPlan> it = nonRepeatplans.iterator();
            while (it.hasNext()) {
                TestPlan plan = (TestPlan) it.next();
                TaskQueue queue = new TaskQueue();
                queue.setPlanId(plan.getId());
                queue.setPlanName(plan.getName());
                queue.setTaskState(TaskState.READY);
                queue.setTriggerTime(plan.getTriggerTime());
                queue.setTestType(plan.getTestType());
                queue.setPriority(plan.getPriority());
                queue.setType(TaskType.FROM_PLAN);
                try {
                    enqueueFromPlan(queue);
                } catch (SQLIntegrityConstraintViolationException | DuplicateKeyException e) {
                }
            }
        }

        criteria.setTriggerTime(null);
        criteria.setRepeatFlag(true);

        List<TestPlan> repeatplans = testPlanMapper.selectBy(criteria);
        if (!CollectionUtils.sizeIsEmpty(repeatplans)) {
            Iterator<TestPlan> it = repeatplans.iterator();
            while (it.hasNext()) {
                TestPlan plan = (TestPlan) it.next();
                LocalDateTime nextTriggerTime = TimeUtils.calculateNextTriggerTime(plan.getCron());
                if (end.isAfter(nextTriggerTime)) {
                    TaskQueue queue = new TaskQueue();
                    queue.setPlanId(plan.getId());
                    queue.setPlanName(plan.getName());
                    queue.setTaskState(TaskState.READY);
                    queue.setTriggerTime(nextTriggerTime);
                    queue.setTestType(plan.getTestType());
                    queue.setPriority(plan.getPriority());
                    queue.setType(TaskType.FROM_PLAN);
                    try {
                        enqueueFromPlan(queue);
                    } catch (SQLIntegrityConstraintViolationException | DuplicateKeyException e) {
                    }
                }
            }
        }
        
        changeToDelay();
    }

    private void enqueueFromPlan(TaskQueue queue) throws SQLIntegrityConstraintViolationException, DuplicateKeyException {
        taskQueueMapper.insert(queue);
    }

    private void changeToDelay() {
        TaskQueue delayedCriteria = new TaskQueue();
        delayedCriteria.setTaskState(TaskState.DELAYED);
        delayedCriteria.setTriggerTime(LocalDateTime.now());
        taskQueueMapper.changeToDelay(delayedCriteria);
    }
}
