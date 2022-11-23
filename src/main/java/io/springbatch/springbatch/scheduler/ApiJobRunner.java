package io.springbatch.springbatch.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.quartz.JobBuilder.newJob;

@Component
public class ApiJobRunner extends JobRunner
{
    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args)
    {
        JobDetail jobDetail = buildJobDetail(ApiSchJob.class, "apiJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0/05 * * * * ?"); // cron expression

        try
        {
            scheduler.scheduleJob(jobDetail, trigger);
        }
        catch( SchedulerException e)
        {
            e.printStackTrace();
        }
    }
}
