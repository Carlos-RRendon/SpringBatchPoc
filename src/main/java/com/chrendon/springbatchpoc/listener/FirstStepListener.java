package com.chrendon.springbatchpoc.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FirstStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("Before Step: {}", stepExecution.getStepName());
        log.debug("Before Job Exec Ctx: {}", stepExecution.getJobExecution().getExecutionContext());
        log.debug("Before Step Exec Ctx: {}", stepExecution.getExecutionContext());

        stepExecution.getExecutionContext().put("sec","sec value");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.debug("After Step: {}", stepExecution.getStepName());
        log.debug("After Job Exec Ctx Step: {}", stepExecution.getJobExecution().getExecutionContext());
        log.debug("After Step Exec Ctx: {}", stepExecution.getExecutionContext());
        return null;
    }
}
