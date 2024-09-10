package com.chrendon.springbatchpoc.reader;

import com.chrendon.springbatchpoc.configuration.property.CsvFileReaderProps;
import com.chrendon.springbatchpoc.model.BloqueoCsv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BloqueoItemReader {

    private final CsvFileReaderProps fileReaderProps;

    public FlatFileItemReader<BloqueoCsv> flatFileItemReader() {
        FlatFileItemReader<BloqueoCsv> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource(fileReaderProps.getFilePath());
        DefaultLineMapper<BloqueoCsv> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(fileReaderProps.getDefaultLineDelimiter());
        tokenizer.setNames(fileReaderProps.getColumnNames().toArray(new String[0]));

        lineMapper.setLineTokenizer(tokenizer);


        BeanWrapperFieldSetMapper<BloqueoCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(BloqueoCsv.class);

        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(lineMapper);
        flatFileItemReader.setLinesToSkip(fileReaderProps.getSkipLines());

        return flatFileItemReader;
    }


}
