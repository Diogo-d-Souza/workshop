package com.technical.workshop.repositories;

import com.technical.workshop.model.CarMaintenance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMaintenanceRepository extends MongoRepository<CarMaintenance, String> {

}
