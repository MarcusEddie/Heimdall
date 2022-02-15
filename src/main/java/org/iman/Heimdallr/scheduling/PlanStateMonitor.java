package org.iman.Heimdallr.scheduling;

import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.Consts;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.mapper.TaskQueueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlanStateMonitor {

    private static final Logger log = LoggerFactory.getLogger(PlanScheduler.class);
    
    @Autowired
    private TaskQueueMapper taskQueueMapper;
    
    @Scheduled(fixedRate = 2000)
    public void stateMonitor() {
        log.info("[State Monitor] Plan state monitor starts at "+ LocalDateTime.now().toString());
        TaskQueue delayedCriteria = new TaskQueue();
        delayedCriteria.setTaskState(TaskState.DELAYED);
        delayedCriteria.setTriggerTime(LocalDateTime.now());
        delayedCriteria.setModifiedBy(Consts.SYSTEM_ADMIN);
        int cnt = taskQueueMapper.changeToDelay(delayedCriteria);
        log.info("[State Monitor] Total {} plans changed to delay ", cnt);
    }
}
