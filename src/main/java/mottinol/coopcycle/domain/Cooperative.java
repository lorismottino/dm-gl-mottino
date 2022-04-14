package mottinol.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.domain.Persistable;

/**
 * A Cooperative.
 */
@Entity
@Table(name = "cooperative")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cooperative")
public class Cooperative implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Transient
    private boolean isPersisted;

    @OneToMany(mappedBy = "cooperative")
    @JsonIgnoreProperties(value = { "orders", "cooperative" }, allowSetters = true)
    private Set<Courier> couriers = new HashSet<>();

    @ManyToMany(mappedBy = "cooperatives")
    @JsonIgnoreProperties(value = { "products", "cooperatives" }, allowSetters = true)
    private Set<Restaurant> restaurants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cooperative id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cooperative name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Cooperative setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public Set<Courier> getCouriers() {
        return this.couriers;
    }

    public void setCouriers(Set<Courier> couriers) {
        if (this.couriers != null) {
            this.couriers.forEach(i -> i.setCooperative(null));
        }
        if (couriers != null) {
            couriers.forEach(i -> i.setCooperative(this));
        }
        this.couriers = couriers;
    }

    public Cooperative couriers(Set<Courier> couriers) {
        this.setCouriers(couriers);
        return this;
    }

    public Cooperative addCourier(Courier courier) {
        this.couriers.add(courier);
        courier.setCooperative(this);
        return this;
    }

    public Cooperative removeCourier(Courier courier) {
        this.couriers.remove(courier);
        courier.setCooperative(null);
        return this;
    }

    public Set<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.removeCooperative(this));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.addCooperative(this));
        }
        this.restaurants = restaurants;
    }

    public Cooperative restaurants(Set<Restaurant> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Cooperative addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.getCooperatives().add(this);
        return this;
    }

    public Cooperative removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.getCooperatives().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
