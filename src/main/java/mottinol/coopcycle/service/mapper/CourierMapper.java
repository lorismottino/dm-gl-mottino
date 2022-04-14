package mottinol.coopcycle.service.mapper;

import mottinol.coopcycle.domain.Cooperative;
import mottinol.coopcycle.domain.Courier;
import mottinol.coopcycle.service.dto.CooperativeDTO;
import mottinol.coopcycle.service.dto.CourierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Courier} and its DTO {@link CourierDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourierMapper extends EntityMapper<CourierDTO, Courier> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    CourierDTO toDto(Courier s);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);
}
