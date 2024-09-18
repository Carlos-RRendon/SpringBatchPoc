package com.chrendon.springbatchpoc.configuration;

import com.chrendon.springbatchpoc.listener.FirstJobListener;
import com.chrendon.springbatchpoc.listener.FirstStepListener;
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
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
//@EnableBatchProcessing
public class SampleJob {


    private final SecondTasklet secondTasklet;
    private final FirstJobListener firstJobListener;
    private final FirstStepListener firstStepListener;


//    private final DataSourceTransactionManager transactionManager;



    @Bean
    public Step secondStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .listener(firstStepListener)
                .build();
    }

    @Bean
    public Job secondJob(JobRepository jobRepository,  Step firstChunkStep, Step secondStep) {
        return new JobBuilder("Second Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep)
                .next(secondStep)
                .listener(firstJobListener)
                .build();
    }


}
