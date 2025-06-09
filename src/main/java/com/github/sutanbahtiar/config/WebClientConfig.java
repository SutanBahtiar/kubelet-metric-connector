package com.github.sutanbahtiar.config;

/*
 * @author sutan.bahtiar@gmail.com
 */

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final AppConfig appConfig;

    @Bean
    public WebClient webClientSandbox() throws SSLException {
        return WebClient.builder()
                .baseUrl(appConfig.getKubelet().getBaseSandboxUrl())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .codecs(getClientCodecConfigurerConsumer())
                .build();
    }

    @Bean
    public WebClient webClientProduction() throws SSLException {
        return WebClient.builder()
                .baseUrl(appConfig.getKubelet().getBaseProductionUrl())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .codecs(getClientCodecConfigurerConsumer())
                .build();
    }

    private static Consumer<ClientCodecConfigurer> getClientCodecConfigurerConsumer() {
        final long size = DataSize.ofGigabytes(1).toBytes();
        return clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize((int) size);
    }

    private HttpClient getHttpClient() throws SSLException {
        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, appConfig.getHttpClient().getConnectTimeout())
                .doOnConnected(connection -> connection
                        .addHandlerFirst(new ReadTimeoutHandler(appConfig.getHttpClient().getReadTimeout()))
                        .addHandlerFirst(new WriteTimeoutHandler(appConfig.getHttpClient().getWriteTimeout())))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
    }
}
