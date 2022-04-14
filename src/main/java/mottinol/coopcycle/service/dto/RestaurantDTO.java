package mottinol.coopcycle.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mottinol.coopcycle.domain.Restaurant} entity.
 */
public class RestaurantDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 300)
    private String address;

    private Set<CooperativeDTO> cooperatives = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CooperativeDTO> getCooperatives() {
        return cooperatives;
    }

    public void setCooperatives(Set<CooperativeDTO> cooperatives) {
        this.cooperatives = cooperatives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantDTO)) {
            return false;
        }

        RestaurantDTO restaurantDTO = (RestaurantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", cooperatives=" + getCooperatives() +
            "}";
    }
}
