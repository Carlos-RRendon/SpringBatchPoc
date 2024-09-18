package com.chrendon.springbatchpoc.service;

import com.chrendon.springbatchpoc.configuration.property.CsvFileReaderProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecondTasklet implements Tasklet {

    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;
    private final CsvFileReaderProps csvFileReaderProps;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.debug("This is the second tasklet step");

        sftpRemoteFileTemplate.execute(session -> {

            if (session.exists(csvFileReaderProps.getPath())) {
                log.debug("The file {} already exists", csvFileReaderProps.getPath());
            }
            return null;
        });

        log.debug("Second step context: {}", chunkContext.getStepContext().getJobExecutionContext());
        return RepeatStatus.FINISHED;
    }
}
