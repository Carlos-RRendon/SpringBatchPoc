package com.chrendon.springbatchpoc.reader;

import com.chrendon.springbatchpoc.configuration.property.CsvFileReaderProps;
import com.chrendon.springbatchpoc.model.BloqueoCsv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BloqueoItemReader {

    private final CsvFileReaderProps fileReaderProps;


    @Bean
    public FlatFileItemReader<BloqueoCsv> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<BloqueoCsv>()
                .name("bloqueoSCVItemReader")
                .resource(fileReaderProps.getUri())
                .delimited()
                .delimiter(fileReaderProps.getLineDelimiter())
                .names(fileReaderProps.getColumnNames().toArray(new String[0]))
                .targetType(BloqueoCsv.class)
                .linesToSkip(fileReaderProps.getSkipLines())
                .build();


    }


}
