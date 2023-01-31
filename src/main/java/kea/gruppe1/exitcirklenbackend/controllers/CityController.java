package kea.gruppe1.exitcirklenbackend.controllers;

import com.microsoft.graph.requests.GraphServiceClient;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.City;
import kea.gruppe1.exitcirklenbackend.repositories.CityRepository;
import kea.gruppe1.exitcirklenbackend.services.EmailService;
import kea.gruppe1.exitcirklenbackend.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class CityController {

    @Autowired
    CityRepository cityRepository;


    @Autowired
    GraphService graphService;

    @Autowired
    EmailService emailService;

    @GetMapping("/")
    @ResponseBody
    public String getget(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph) {

        Applicant applicant = new Applicant();
        applicant.setEmail("sverrireinert@gmail.com");

        emailService.sendWelcomeEmail(applicant, graph);
        return "";
    }

    /**
     * Gets all cities
     * @return a list of cities
     */
    @GetMapping("/cities")
    public List<City> getCities(@AuthenticationPrincipal AuthenticationPrincipal a) {
        System.out.println(a);
        return cityRepository.findAll();
    }

    /**
     * Gets a single city from the id
     * @param id the specified city id
     * @return the city or null if no city has the id
     */
    @GetMapping("/cities/{id}")
    public City getCity(@PathVariable Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new city to the database
     * @param newCity the data to save
     * @return the saved city
     */
    @PostMapping("/cities")
    public City addCity(@RequestBody City newCity) {
        return cityRepository.save(newCity);
    }

    /**
     * Replaces a city in the database with new data
     * @param id the id of the city that is replaced
     * @param cityToUpdateWith the new city data
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
    @PutMapping("/cities/{id}")
    public HttpStatus updateCityById(@PathVariable Long id, @RequestBody City cityToUpdateWith) {
        if (cityRepository.existsById(id)) {
            System.out.println(cityToUpdateWith.getName());
            if (!cityToUpdateWith.getId().equals(id)) {
                cityRepository.deleteById(id);
            }
            cityRepository.save(cityToUpdateWith);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    /**
     * Update data on a city in the database
     * @param id the id of the city
     * @param cityToUpdateWith the city object with the new data
     * @return a string with a success or failed message
     */
    @PatchMapping("/cities/{id}")
    public String PatchCityById(@PathVariable Long id, @RequestBody City cityToUpdateWith) {
        cityRepository.findById(id).map(foundCity -> {
            if (cityToUpdateWith.getName() != null)
                foundCity.setName(cityToUpdateWith.getName());
            if (cityToUpdateWith.getAddress() != null)
                foundCity.setAddress(cityToUpdateWith.getAddress());

            cityRepository.save(foundCity);
            return "City was updated";
        });
        return "City was not found";
    }


    /**
     * Deletes a specific city
     * @param id the id of the city
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
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
