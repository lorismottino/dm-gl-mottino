package mottinol.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import mottinol.coopcycle.domain.Courier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Courier} entity.
 */
public interface CourierSearchRepository extends ElasticsearchRepository<Courier, String>, CourierSearchRepositoryInternal {}

interface CourierSearchRepositoryInternal {
    Stream<Courier> search(String query);
}

class CourierSearchRepositoryInternalImpl implements CourierSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CourierSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Courier> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Courier.class).map(SearchHit::getContent).stream();
    }
}
