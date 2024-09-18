package com.chrendon.springbatchpoc.configuration;


import com.chrendon.springbatchpoc.configuration.property.SftpConnectionProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpStreamingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.transformer.StreamTransformer;
import org.springframework.messaging.MessageHandler;

import java.io.InputStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SftpConnectionConfig {

    private final SftpConnectionProps connectionProps;



    @Bean
    public DefaultSftpSessionFactory defaultSftpClientFactory() {
        DefaultSftpSessionFactory sessionFactory = new DefaultSftpSessionFactory(true);
        sessionFactory.setHost(connectionProps.getHost());
        sessionFactory.setPort(connectionProps.getPort());
        sessionFactory.setUser(connectionProps.getUser());
        sessionFactory.setPrivateKey(connectionProps.getPrivateKey());
        sessionFactory.setPrivateKeyPassphrase(connectionProps.getPassphrase());
        return sessionFactory;
    }

    @Bean
    public CachingSessionFactory<SftpClient.DirEntry> cachingSessionFactory() {
        CachingSessionFactory<SftpClient.DirEntry> cachingSessionFactory = new CachingSessionFactory<>(defaultSftpClientFactory(), 10);
        cachingSessionFactory.setSessionWaitTimeout(1000);
        cachingSessionFactory.setPoolSize(10);
        return cachingSessionFactory;
    }


    @Bean
    @InboundChannelAdapter(channel = "stream")
    public MessageSource<InputStream> sftpMessageSource() {
        SftpStreamingMessageSource source = new SftpStreamingMessageSource(sftpRemoteFileTemplate());
        source.setRemoteDirectory(connectionProps.getRemotePath());
        source.setFilter(new SftpSimplePatternFileListFilter("*.csv"));
        source.setMaxFetchSize(1);
        return source;
    }

    @Bean
    @Transformer(inputChannel = "stream", outputChannel = "data")
    public org.springframework.integration.transformer.Transformer transformer() {
        return new StreamTransformer("UTF-8");
    }

    @Bean
    public SftpRemoteFileTemplate sftpRemoteFileTemplate() {
        return new SftpRemoteFileTemplate(defaultSftpClientFactory());
    }

    @ServiceActivator(inputChannel = "data", adviceChain = "after")
    @Bean
    MessageHandler handler() {
        return System.out::println;
    }

    @Bean
    public ExpressionEvaluatingRequestHandlerAdvice after() {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        advice.setOnSuccessExpressionString("@template.remove(headers['file_remoteDirectory'] + '/' +  headers['file_remoteFile'])");
        advice.setPropagateEvaluationFailures(true);
        return advice;
    }

}
