package com.chrendon.springbatchpoc.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
//@EnableBatchProcessing
public class SampleJob {

    private final DataSource batchDataSource;


    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(batchDataSource);
    }


    @Bean
    public Job firstJob(JobRepository jobRepository) {
        return new JobBuilder("firstJob", jobRepository)
                .start(firstStep(jobRepository))
                .build();

    }

    private Step firstStep(JobRepository jobRepository) {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(firstTask(), transactionManager())
                .build();
    }

    private Tasklet firstTask() {
        return (StepContribution contribution, ChunkContext chunkContext) -> {
            log.debug("This is the first tasklet step");
            return RepeatStatus.FINISHED;
        };
    }

}
