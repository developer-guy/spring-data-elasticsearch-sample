package com.elastic.data.elastic.config;


import com.elastic.data.elastic.data.service.BookService;
import com.elastic.data.elastic.data.service.impl.BookServiceImpl;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@TestConfiguration
public class ElasticSearchTestConfig {


    @Value("${spring.data.elasticsearch.cluster-name:elasticsearch_devquy}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.properties.path.home:/usr/local/Cellar/elasticsearch/6.2.3/}")
    private String pathHome;

    @Value("${spring.data.elasticsearch.properties.path.log:/tmp/log}")
    private String pathLog;

    //Embedded Elasticsearch Server
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() throws NodeValidationException {
        return new ElasticsearchTemplate(elasticSearchTestNode().client());
    }


    @Bean
    public Node elasticSearchTestNode() throws NodeValidationException {
        Settings.Builder settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("http.enabled", false)
                .put("transport.type", "local")
                .put("path.home", pathHome)
                .put("path.logs", pathLog);
        Node node = new Node(settings.build());
        node.start();
        return node;
    }

    @Bean
    public BookService bookService() {
        return new BookServiceImpl();
    }

}
