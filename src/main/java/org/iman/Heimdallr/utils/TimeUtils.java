package org.iman.Heimdallr.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

public class TimeUtils {

    public static LocalDateTime calculateNextTriggerTime(String cronStr) {
        CronDefinition cronDefinition = CronDefinitionBuilder
                .instanceDefinitionFor(CronType.QUARTZ);
        // Create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cronStr));
        ZonedDateTime now = ZonedDateTime.now();
        Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
        return nextExecution.get().toLocalDateTime();
    }
    
    public static String cronDescription(String cronStr) {
        CronDefinition cronDefinition = CronDefinitionBuilder
                .instanceDefinitionFor(CronType.QUARTZ);
        // Create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);
        // Create a descriptor for a specific Locale
        CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
        String description = descriptor.describe(parser.parse(cronStr));
//         "1 2 0,12 3 4 ? 2021-2023"
        return description;
    }
}
