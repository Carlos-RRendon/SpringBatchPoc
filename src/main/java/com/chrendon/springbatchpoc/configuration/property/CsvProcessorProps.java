package com.chrendon.springbatchpoc.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("config.csv-processor")
@Getter
@Setter
public class CsvProcessorProps {
    private int chunkSize;
}
