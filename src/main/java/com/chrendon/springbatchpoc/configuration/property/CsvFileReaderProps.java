package com.chrendon.springbatchpoc.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
@ConfigurationProperties("csv")
@Getter
@Setter
public class CsvFileReaderProps {
    private Resource filePath;
    private String defaultLineDelimiter;
    private List<String> columnNames;
    private int skipLines;
}
