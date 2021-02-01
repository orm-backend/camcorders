package ru.netris.camcorders.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfiguration {
    
    static final int DEFAULT_KEEP_ALIVE_TIME = 75_000;
    
    static final int MAX_TOTAL_CONNECTIONS = 8192;
    
    static final int MAX_ROUTE_CONNECTIONS = 8192;
    
    static final int MAX_LOCALHOST_CONNECTIONS = 16384;

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
      return (httpResponse, httpContext) -> {
        HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
        HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);
          
        while (elementIterator.hasNext()) {
          HeaderElement element = elementIterator.nextElement();
          String param = element.getName();
          String value = element.getValue();
          if (value != null && param.equalsIgnoreCase("timeout")) {
            return Long.parseLong(value) * 1000; // convert to ms
          }
        }
          
        return DEFAULT_KEEP_ALIVE_TIME;
      };
    }
    
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
      PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
      // set a total amount of connections across all HTTP routes
      poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
      // set a maximum amount of connections for each HTTP route in pool
      poolingConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
      // increase the amounts of connections if the host is localhost
      HttpHost localhost = new HttpHost("http://localhost", 8080);
      poolingConnectionManager.setMaxPerRoute(new HttpRoute(localhost), MAX_LOCALHOST_CONNECTIONS);

      return poolingConnectionManager;
    }
    
    @Bean
    public CloseableHttpClient httpClient() {
//      RequestConfig requestConfig = RequestConfig.custom()
//              .setConnectTimeout(CONNECT_TIMEOUT)
//              .setConnectionRequestTimeout(REQUEST_TIMEOUT)
//              .setSocketTimeout(SOCKET_TIMEOUT)
//              .build();


      return HttpClients.custom()
              .setDefaultRequestConfig(RequestConfig.DEFAULT)
              .setConnectionManager(poolingConnectionManager())
              .setKeepAliveStrategy(connectionKeepAliveStrategy())
              .build();
    }
    
    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(CloseableHttpClient httpClient) {
	return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
    
}
