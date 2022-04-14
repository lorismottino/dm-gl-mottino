package mottinol.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mottinol.coopcycle.domain.Courier} entity.
 */
public class CourierDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-z\\- ]+$")
    private String firstname;

    @NotNull
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-z\\- ]+$")
    private String lastname;

    @NotNull
    private String phone;

    private String email;

    private CooperativeDTO cooperative;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourierDTO)) {
            return false;
        }

        CourierDTO courierDTO = (CourierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourierDTO{" +
            "id='" + getId() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
