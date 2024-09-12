package com.chrendon.springbatchpoc.service;

import com.chrendon.springbatchpoc.configuration.property.CsvProcessorProps;
import com.chrendon.springbatchpoc.model.BloqueoCsv;
import com.chrendon.springbatchpoc.writter.FirstItemWritter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChuckStepConf {


    private final FirstItemWritter firstItemWritter;
    private final CsvProcessorProps csvProcessorProps;

    @Bean
    public Step firstChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                               FlatFileItemReader<BloqueoCsv> flatFileItemReader) {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<BloqueoCsv, BloqueoCsv>chunk(csvProcessorProps.getChunkSize(), transactionManager)
                .reader(flatFileItemReader)
//                .processor(firstItemProcessor)
                .writer(firstItemWritter)
                .build();
    }
}
