package com.chrendon.springbatchpoc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BloqueoCsv {

    private String entityCode;
    private String blackListType;
    private String blackListValue;
    private String lastModified;
    private String lastUpdated;

}
