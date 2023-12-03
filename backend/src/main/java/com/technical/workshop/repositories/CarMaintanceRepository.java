package com.technical.workshop.repositories;

import com.technical.workshop.model.CarMaintance;
import com.technical.workshop.model.ServiceCar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMaintanceRepository extends MongoRepository<CarMaintance, String> {

}
