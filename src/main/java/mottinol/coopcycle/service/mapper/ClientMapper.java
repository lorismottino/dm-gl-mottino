package mottinol.coopcycle.service.mapper;

import mottinol.coopcycle.domain.Client;
import mottinol.coopcycle.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
