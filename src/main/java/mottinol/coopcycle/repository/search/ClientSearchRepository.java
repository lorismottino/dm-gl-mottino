package mottinol.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import mottinol.coopcycle.domain.Client;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Client} entity.
 */
public interface ClientSearchRepository extends ElasticsearchRepository<Client, String>, ClientSearchRepositoryInternal {}

interface ClientSearchRepositoryInternal {
    Stream<Client> search(String query);
}

class ClientSearchRepositoryInternalImpl implements ClientSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ClientSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Client> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Client.class).map(SearchHit::getContent).stream();
    }
}
