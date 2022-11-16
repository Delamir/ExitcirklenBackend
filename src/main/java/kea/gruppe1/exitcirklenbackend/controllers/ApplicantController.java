package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class ApplicantController {

    @Autowired
    ApplicantRepository applicantRepository;

    /**
     * Gets all applicants in the database
     */
    @GetMapping("/applicants")
    public List<Applicant> getApplicants() {
        return applicantRepository.findAll();
    }

    /**
     * Get a single applicant from id
     * @param id The id from the applicant
     * @return Applicant with provided id or return null
     */
    @GetMapping("/applicant/{id}")
    public Applicant getApplicant(@PathVariable Long id) {
        return applicantRepository.findById(id).orElse(null);
    }

    /**
     * Gets all applicants from a specific status
     * @param status is the wanted status
     * @return A list of all applicants with the status in the parameter
     */
    @GetMapping("/applicants/status/{status}")
    public List<Applicant> getApplicantsByStatus(@PathVariable String status) {
        return applicantRepository.findApplicantByStatus(status);
    }

    /**
     * Gets all applicants from a specified city
     * @param city is the wanted city
     * @return A list of all applicants with the specified city
     */
    @GetMapping("/applicants/by/{city}")
    public List<Applicant> getApplicantsByCity(@PathVariable String city) {
        return applicantRepository.findApplicantByCity(city);
    }

    @PostMapping("/applicants")
    public Applicant createApplicant(@RequestBody Applicant newApplicant){
        System.out.println(newApplicant.getEmail());
        return applicantRepository.save(newApplicant);
    }
}
