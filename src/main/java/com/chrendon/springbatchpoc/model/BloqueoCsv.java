package com.chrendon.springbatchpoc.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
public class BloqueoCsv {

    private String entityCode;
    private String blackListType;
    private String blackListValue;
    private Date lastModified;
    private Date lastUpdated;
}
