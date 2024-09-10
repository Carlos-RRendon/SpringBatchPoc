package com.chrendon.springbatchpoc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

//@Service
@Slf4j
public class SecondJobScheduler {


    private final JobLauncher jobLauncher;


    private final Job secondJob;

    public SecondJobScheduler(JobLauncher jobLauncher, @Qualifier("secondJob") Job secondJob) {
        this.jobLauncher = jobLauncher;
        this.secondJob = secondJob;
    }

    @Scheduled(cron = "0 * * * * *")
    public void secondJobScheduler() {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("currentTime", System.currentTimeMillis());
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(secondJob, jobParameters);
            log.debug("Job Execution ID: {}", jobExecution.getId());
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            log.error("Error executing job", e);
        }

    }
}
