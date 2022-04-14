package mottinol.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import mottinol.coopcycle.domain.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Order} entity.
 */
public interface OrderSearchRepository extends ElasticsearchRepository<Order, String>, OrderSearchRepositoryInternal {}

interface OrderSearchRepositoryInternal {
    Stream<Order> search(String query);
}

class OrderSearchRepositoryInternalImpl implements OrderSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    OrderSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Order> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Order.class).map(SearchHit::getContent).stream();
    }
}
