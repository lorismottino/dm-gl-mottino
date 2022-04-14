package mottinol.coopcycle.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mottinol.coopcycle.domain.Cooperative;
import mottinol.coopcycle.domain.Restaurant;
import mottinol.coopcycle.service.dto.CooperativeDTO;
import mottinol.coopcycle.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
    @Mapping(target = "cooperatives", source = "cooperatives", qualifiedByName = "cooperativeIdSet")
    RestaurantDTO toDto(Restaurant s);

    @Mapping(target = "removeCooperative", ignore = true)
    Restaurant toEntity(RestaurantDTO restaurantDTO);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);

    @Named("cooperativeIdSet")
    default Set<CooperativeDTO> toDtoCooperativeIdSet(Set<Cooperative> cooperative) {
        return cooperative.stream().map(this::toDtoCooperativeId).collect(Collectors.toSet());
    }
}
