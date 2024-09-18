package com.chrendon.springbatchpoc.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Getter
@Setter
@ConfigurationProperties("config.sftp-connection")
public class SftpConnectionProps {

    private String host;
    private int port;
    private String user;
    private String remotePath;
    private Resource privateKey;
    private String passphrase;
}
