package com.chrendon.springbatchpoc.service;

import com.chrendon.springbatchpoc.model.BloqueoCsv;
import com.chrendon.springbatchpoc.reader.BloqueoItemReader;
import com.chrendon.springbatchpoc.writter.FirstItemWritter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChuckStepConf {


    private final FirstItemWritter firstItemWritter;
    private final BloqueoItemReader bloqueoItemReader;

    @Bean
    public Step firstChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<BloqueoCsv, BloqueoCsv>chunk(3, transactionManager)
                .reader(bloqueoItemReader.flatFileItemReader())
//                .processor(firstItemProcessor)
                .writer(firstItemWritter)
                .build();
    }
}
