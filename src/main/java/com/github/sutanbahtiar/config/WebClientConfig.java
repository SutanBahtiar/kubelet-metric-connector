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
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final AppConfig appConfig;

    @SneakyThrows
    @Bean
    public WebClient webClient() {
        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        final HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, appConfig.getHttpClient().getConnectTimeout())
                .doOnConnected(connection -> connection
                        .addHandlerFirst(new ReadTimeoutHandler(appConfig.getHttpClient().getReadTimeout()))
                        .addHandlerFirst(new WriteTimeoutHandler(appConfig.getHttpClient().getWriteTimeout())))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        final long size = DataSize.ofGigabytes(1).toBytes();

        return WebClient.builder()
                .baseUrl(appConfig.getKubelet().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize((int) size))
                .build();
    }
}
