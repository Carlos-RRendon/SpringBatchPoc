package com.chrendon.springbatchpoc.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
@ConfigurationProperties("config.csv-processor.reader")
@Getter
@Setter
public class CsvFileReaderProps {
    private String path;
    private Resource uri;
    private String lineDelimiter;
    private List<String> columnNames;
    private int skipLines;
}
