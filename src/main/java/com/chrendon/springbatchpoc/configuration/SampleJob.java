package com.chrendon.springbatchpoc.configuration;

import com.chrendon.springbatchpoc.configuration.property.CsvFileReaderProps;
import com.chrendon.springbatchpoc.listener.FirstJobListener;
import com.chrendon.springbatchpoc.listener.FirstStepListener;
import com.chrendon.springbatchpoc.model.BloqueoCsv;
import com.chrendon.springbatchpoc.processor.FirstItemProcessor;
import com.chrendon.springbatchpoc.reader.FirstItemReader;
import com.chrendon.springbatchpoc.service.FirstTasklet;
import com.chrendon.springbatchpoc.service.SecondTasklet;
import com.chrendon.springbatchpoc.writter.FirstItemWritter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
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
    private final FirstStepListener firstStepListener;
    private final CsvFileReaderProps fileReaderProps;

    /**
     * Chunk oriented step
     */
    private final FirstItemReader firstItemReader;
    private final FirstItemProcessor firstItemProcessor;
    private final FirstItemWritter firstItemWritter;

    private final DataSourceTransactionManager transactionManager;

    //    @Bean
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
                .tasklet(firstTasklet, transactionManager)
                .listener(firstStepListener)
                .build();
    }


    private Step secondStep(JobRepository jobRepository) {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job secondJob(JobRepository jobRepository) {
        return new JobBuilder("Second Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep(jobRepository))
                .next(secondStep(jobRepository))
                .build();
    }

    private Step firstChunkStep(JobRepository jobRepository) {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<BloqueoCsv, BloqueoCsv>chunk(3, transactionManager)
                .reader(flatFileItemReader())
//                .processor(firstItemProcessor)
                .writer(firstItemWritter)
                .build();
    }

    public FlatFileItemReader<BloqueoCsv> flatFileItemReader() {
        FlatFileItemReader<BloqueoCsv> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource(fileReaderProps.getFilePath());
        DefaultLineMapper<BloqueoCsv> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(fileReaderProps.getDefaultLineDelimiter());
        tokenizer.setNames(fileReaderProps.getColumnNames().toArray(new String[0]));

        lineMapper.setLineTokenizer(tokenizer);


        BeanWrapperFieldSetMapper<BloqueoCsv> beanWrapperFieldSetMapper= new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(BloqueoCsv.class);

        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(lineMapper);
        flatFileItemReader.setLinesToSkip(fileReaderProps.getSkipLines());

        return flatFileItemReader;
    }

}
