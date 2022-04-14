package mottinol.coopcycle.service.mapper;

import mottinol.coopcycle.domain.Client;
import mottinol.coopcycle.domain.Courier;
import mottinol.coopcycle.domain.Order;
import mottinol.coopcycle.service.dto.ClientDTO;
import mottinol.coopcycle.service.dto.CourierDTO;
import mottinol.coopcycle.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "courier", source = "courier", qualifiedByName = "courierId")
    OrderDTO toDto(Order s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("courierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourierDTO toDtoCourierId(Courier courier);
}
