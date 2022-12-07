package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.City;
import kea.gruppe1.exitcirklenbackend.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class CityController {

    @Autowired
    CityRepository cityRepository;

    @GetMapping("/cities")
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    @GetMapping("/city/{id}")
    public City getCity(@PathVariable Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    @PostMapping("/cities")
    public City addCity(@RequestBody City newCity) {
        return cityRepository.save(newCity);
    }

    @PutMapping("/cities/{id}")
    public HttpStatus updateCityById(@PathVariable Long id, @RequestBody City cityToUpdateWith) {
        if (cityRepository.existsById(id)) {
            if (!cityToUpdateWith.getId().equals(id)) {
                cityRepository.deleteById(id);
            }
            cityRepository.save(cityToUpdateWith);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @DeleteMapping("/cities/{id}")
    public HttpStatus deleteCityById(@PathVariable Long id) {
        try {
            cityRepository.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }
}