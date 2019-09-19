package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ShipService {

    private final ShipRepository shipRepository;

    @Contract(pure = true)
    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Page<Ship> getAll(
            Long id,
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating,
            @NotNull ShipOrder order,
            Integer pageNumber,
            Integer pageSize){
        Specification spec = createSpecification(id, name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        return shipRepository.findAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));

    }

    public Integer getCount(
            Long id,
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating){
        Specification spec = createSpecification(id, name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipRepository.findAll(spec).size();
    }

    public Ship createShip(String name, String planet,
                           ShipType shiptype, Long prodDateLong,
                           Boolean isUsed, Double speed,
                           Integer crewSize){

        Double rating = calcRating(speed, prodDateLong, isUsed);

        return shipRepository.save(new Ship(name, planet, shiptype, new Date(prodDateLong), isUsed, speed, crewSize, rating));
    }


    public Ship getShip (Long id){
        return shipRepository.getById(id);
    }


    public Ship updateShip(@NotNull Ship ship){
        Double rating = calcRating(ship.getSpeed(), ship.getProdDate().getTime(), ship.getUsed());
        ship.setRating(rating);
        return shipRepository.save(ship);
    }


    public void deleteShip(Ship ship){
        shipRepository.delete(ship);
    }


    private Double calcRating(Double speed, Long prodDateLong, Boolean isUsed){
        double k = isUsed ? 0.5 : 1;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(prodDateLong);
        Double rating = 80 * speed * k / (3019 - c.get(Calendar.YEAR) + 1);
        rating = Math.round(rating * 100)/100.0;

        return rating;
    }


    private Specification<Ship> createSpecification(
            Long id,
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating){
        ShipSpecificationsBuilder builder = new ShipSpecificationsBuilder();
        if(id != null){
            builder.with("id", ":", id);
        }

        if(name != null){
            builder.with("name", ":", name);
        }

        if(planet != null){
            builder.with("planet", ":", planet);
        }

        if (shipType != null){
            builder.with("shipType", ":", shipType);
        }

        if (isUsed != null) {
            builder.with("isUsed", ":", isUsed);
        }

        if(minSpeed != null){
            builder.with("speed", ">", minSpeed);
        }

        if(maxSpeed != null){
            builder.with("speed", "<", maxSpeed);
        }

        if(minCrewSize != null) {
            builder.with("crewSize", ">", minCrewSize);
        }

        if(maxCrewSize != null) {
            builder.with("crewSize", "<", maxCrewSize);
        }

        if(minRating != null) {
            builder.with("rating", ">", minRating);
        }

        if(maxRating != null) {
            builder.with("rating", "<", maxRating);
        }

        if(after != null) {
            builder.with("prodDate", ">", new Date(after));
        }

        if(before != null) {
            builder.with("prodDate", "<", new Date(before));
        }

        Specification<Ship> spec = builder.build();
        return spec;
    }


}
