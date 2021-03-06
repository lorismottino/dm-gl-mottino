package mottinol.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import mottinol.coopcycle.domain.Restaurant;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Restaurant} entity.
 */
public interface RestaurantSearchRepository extends ElasticsearchRepository<Restaurant, String>, RestaurantSearchRepositoryInternal {}

interface RestaurantSearchRepositoryInternal {
    Stream<Restaurant> search(String query);
}

class RestaurantSearchRepositoryInternalImpl implements RestaurantSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    RestaurantSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Restaurant> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Restaurant.class).map(SearchHit::getContent).stream();
    }
}
