package com.example.quartzdemo.test1;

import org.quartz.*;

@DisallowConcurrentExecution
public class DatabaseBackupJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        String database = detail.getJobDataMap().getString("database");

        System.out.printf("给数据库 %s 备份, 耗时10秒 %n" ,database);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
