package com.chrendon.springbatchpoc.writter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FirstItemWritter implements ItemWriter<Long> {

    @Override
    public void write(Chunk<? extends Long> chunk) throws Exception {
        log.info("Inside Item Writter");
        chunk.getItems().forEach(aLong -> log.debug("Result to write: {}", aLong));
    }
}
