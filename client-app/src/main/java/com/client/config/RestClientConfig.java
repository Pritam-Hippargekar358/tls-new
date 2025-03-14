package com.client.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RestClientConfig {
    Charset srcCharset = StandardCharsets.UTF_8;//Charset.forName("UTF-8")
    private final SecretsConfig secretsConfig;
//https://community.spiceworks.com/t/client-certificate-authentication-with-java-and-apache-httpclient/1116837/1
    @Bean
    public RestClient restClient() throws Exception {
        //HostnameVerifier hostNameVerifier = NoopHostnameVerifier.INSTANCE;
        //TrustStrategy acceptTrustStrategy = (chains, authType) -> true;
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(secretsConfig.getKeyManagers(), secretsConfig.getTrustManagers(), null);//new java.security.SecureRandom()

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());//NoopHostnameVerifier.INSTANCE

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", socketFactory)
                //.register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();

        //HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(20);// Set max total connections
        connectionManager.setDefaultMaxPerRoute(5);// Set max connections per route
        // Optionally, configure the connection manager with more settings
        // connectionManager.setValidateAfterInactivity(Integer.parseInt(config.getProperty("http.validateAfterInactivity", "10000")));

//        RequestConfig requestConfig = RequestConfig.custom()  //for this https://qiita.com/gosshys/items/df1ea26ba2e8c0860ae4
//                .setConnectTimeout(Integer.parseInt(config.getProperty("http.connectTimeout", "5000")))
//                .setConnectionRequestTimeout(Integer.parseInt(config.getProperty("http.connectionRequestTimeout", "5000")))
//                .setSocketTimeout(Integer.parseInt(config.getProperty("http.socketTimeout", "5000")))
//                .build();

//        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(5, true);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
//                .setDefaultRequestConfig(requestConfig)
//                .setRetryHandler(retryHandler)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        restTemplate.getInterceptors().add(
//                new BasicAuthorizationInterceptor(
//                        username,
//                        password
//                )
//        );

        return RestClient.builder().requestFactory(requestFactory).build();
    }


//    HttpClientResponseHandler can be defined in a separate class or in an anonymous class, but if you define it in a separate class
//    public static String post(final String url, final String jsonBody) throws URISyntaxException {
//        URI uri = new URI(url);
//        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(Timeout.ofMilliseconds(5000L))
//                .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000L))
//                .setResponseTimeout(Timeout.ofMilliseconds(5000L))
//                .build();
//        final BasicCredentialsProvider creedsProvider = new BasicCredentialsProvider();
//        creedsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
//                new UsernamePasswordCredentials("admin", "pass@123".toCharArray()));
//
//        String resultContent = null;
//        // 1. Create HTTP post Method instance
//        HttpPost httpPost = new HttpPost(url);
//        // 2. Set payload and content-type
//        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
//        httpPost.setHeader("Content-type", "application/json");
//        httpPost.addHeader("Content-type", "application/json");
//        // 3. Create HTTP client
//        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
//            // 4. Execute the method through the HTTP client
//            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
//                System.out.println(response.getVersion()); // HTTP/1.1
//                System.out.println(response.getCode()); // 200
//                System.out.println(response.getReasonPhrase()); // OK
//                // 5. Read Response
//                HttpEntity entity = response.getEntity();
//                resultContent = EntityUtils.toString(response.getEntity());
//                log.info("Status Code: " + response.getCode() + " " + response.getReasonPhrase());
//            }
//        } catch (IOException | ParseException ex) {
//            log.error(ex.getMessage(), ex);
//        }
//        return resultContent;
//    }

}
