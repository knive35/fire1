package com.infy.tele.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import java.io.Serializable;
import java.util.Objects;

import static com.infy.tele.config.Constants.ID_DELIMITER;
import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

/**
 * A Employee.
 */
@Document
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PREFIX = "employee";

    @SuppressWarnings("unused")
    @IdPrefix
    private String prefix = PREFIX;
    
    @Id
    @GeneratedValue(strategy = UNIQUE, delimiter = ID_DELIMITER)
    private String id;

    @Field("name")
    private String name;

    @Field("addr")
    private String addr;

    @Field("contact_number")
    private Integer contactNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Employee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public Employee addr(String addr) {
        this.addr = addr;
        return this;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getContactNumber() {
        return contactNumber;
    }

    public Employee contactNumber(Integer contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public void setContactNumber(Integer contactNumber) {
        this.contactNumber = contactNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if (employee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", addr='" + getAddr() + "'" +
            ", contactNumber=" + getContactNumber() +
            "}";
    }
}
