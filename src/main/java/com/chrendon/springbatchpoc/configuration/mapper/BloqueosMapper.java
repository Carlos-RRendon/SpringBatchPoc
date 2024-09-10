/*
package com.chrendon.springbatchpoc.configuration.mapper;

import com.chrendon.springbatchpoc.model.BloqueoCsv;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BloqueosMapper implements FieldSetMapper<BloqueoCsv> {
    @Override
    public BloqueoCsv mapFieldSet(FieldSet fieldSet) throws BindException {
        return BloqueoCsv.builder()
                .entityCode(fieldSet.readString(0))
                .blackListType(fieldSet.readString(1))
                .blackListValue(fieldSet.readString(2))
                .lastModified(fieldSet.readString(0))
                .lastUpdated(fieldSet.readString(1))
                .build();
    }
}
*/
