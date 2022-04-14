package mottinol.coopcycle.repository;

import java.util.List;
import java.util.Optional;
import mottinol.coopcycle.domain.Product;
import org.springframework.data.domain.Page;

public interface ProductRepositoryWithBagRelationships {
    Optional<Product> fetchBagRelationships(Optional<Product> product);

    List<Product> fetchBagRelationships(List<Product> products);

    Page<Product> fetchBagRelationships(Page<Product> products);
}
