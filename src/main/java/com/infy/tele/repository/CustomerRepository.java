package com.infy.tele.repository;

import com.infy.tele.domain.Customer;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends N1qlCouchbaseRepository<Customer, String> {

}
