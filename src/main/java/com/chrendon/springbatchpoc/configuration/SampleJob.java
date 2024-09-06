package com.chrendon.springbatchpoc.configuration;

import com.chrendon.springbatchpoc.listener.FirstJobListener;
import com.chrendon.springbatchpoc.service.FirstTasklet;
import com.chrendon.springbatchpoc.service.SecondTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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
    private final FirstTasklet firstTasklet;
    private final SecondTasklet secondTasklet;
    private final FirstJobListener firstJobListener;


    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(batchDataSource);
    }


    @Bean
    public Job firstJob(JobRepository jobRepository) {
        return new JobBuilder("firstJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep(jobRepository))
                .next(secondStep(jobRepository))
                .listener(firstJobListener)
                .build();

    }

    private Step firstStep(JobRepository jobRepository) {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(firstTasklet, transactionManager())
                .build();
    }


    private Step secondStep(JobRepository jobRepository) {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet, transactionManager())
                .build();
    }


}
