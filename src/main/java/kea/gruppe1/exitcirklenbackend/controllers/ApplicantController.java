package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/applicants/{id}")
    public Applicant getApplicant(@PathVariable Long id) {
        return applicantRepository.findById(id).orElse(null);
    }


}
