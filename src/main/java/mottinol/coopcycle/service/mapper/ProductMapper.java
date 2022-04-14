package mottinol.coopcycle.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mottinol.coopcycle.domain.Order;
import mottinol.coopcycle.domain.Product;
import mottinol.coopcycle.domain.Restaurant;
import mottinol.coopcycle.service.dto.OrderDTO;
import mottinol.coopcycle.service.dto.ProductDTO;
import mottinol.coopcycle.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "orders", source = "orders", qualifiedByName = "orderIdSet")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeOrder", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("orderIdSet")
    default Set<OrderDTO> toDtoOrderIdSet(Set<Order> order) {
        return order.stream().map(this::toDtoOrderId).collect(Collectors.toSet());
    }

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);
}
