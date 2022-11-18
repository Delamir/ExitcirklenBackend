package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        newApplicant.setStatus("IKKE VISITERET");
        return applicantRepository.save(newApplicant);
    }

    /**
     * Updates only the changed values on the requested applicant
     * @param id the specific applicants id
     * @param applicantToUpdate the applicant object which needs to be updated
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
    @PatchMapping("/applicants/{id}")
    public HttpStatus updateApplicants(@PathVariable Long id,
                                       @RequestBody Applicant applicantToUpdate) {

        applicantRepository.findById(id).map(applicant -> {
            if (applicant.getName() != null) applicant.setName(applicantToUpdate.getName());
            if (applicant.getAge() != null) applicant.setAge(applicantToUpdate.getAge());
            if (applicant.getGender() != null) applicant.setGender(applicantToUpdate.getGender());
            if (applicant.getEmail() != null) applicant.setEmail(applicantToUpdate.getEmail());
            if (applicant.getPhoneNumber() != null) applicant.setPhoneNumber(applicantToUpdate.getPhoneNumber());
            if (applicant.getCity() != null) applicant.setCity(applicantToUpdate.getCity());
            if (applicant.getStatus() != null) applicant.setStatus(applicantToUpdate.getStatus());
            if (applicant.getDescription() != null) applicant.setDescription(applicantToUpdate.getDescription());
            if (applicant.getPriority() != null) applicant.setPriority(applicantToUpdate.getPriority());

            applicant.setContactCall(applicantToUpdate.isContactCall());
            applicant.setContactText(applicantToUpdate.isContactText());
            if (applicant.getLastChanged() != null) applicant.setLastChanged(applicantToUpdate.getLastChanged());

            applicantRepository.save(applicant);
            return HttpStatus.OK;
        });
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Delete a specific applicant
     * @param id the applicant id
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
    @DeleteMapping("/applicants/{id}")
    public HttpStatus deleteApplicant(@PathVariable Long id) {
        try {
            applicantRepository.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }
}
