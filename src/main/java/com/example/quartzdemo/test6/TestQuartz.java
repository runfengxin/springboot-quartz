package com.example.quartzdemo.test6;

import com.example.quartzdemo.job.MailJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class TestQuartz {
    public static void main(String[] args) throws Exception{
        //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        //定义一个触发器
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义名称和所属的租
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(2) //每隔2秒执行一次
                        .withRepeatCount(10)) //总共执行11次(第一次执行不基数)
                .build();

        //定义一个JobDetail
        JobDetail mailJob = newJob(MailJob.class) //指定干活的类MailJob
                .withIdentity("mailjob1", "mailgroup") //定义任务名称和分组
                .usingJobData("email", "admin@10086.com") //定义属性
                .build();

        //增加Job监听
        MailJobListener mailJobListener= new MailJobListener();
        KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(mailJob.getKey());
        scheduler.getListenerManager().addJobListener(mailJobListener, keyMatcher);

        //调度加入这个job
        scheduler.scheduleJob(mailJob, trigger);

        //启动
        scheduler.start();

        //等待20秒，让前面的任务都执行完了之后，再关闭调度器
        Thread.sleep(20000);
        scheduler.shutdown(true);
    }

}
