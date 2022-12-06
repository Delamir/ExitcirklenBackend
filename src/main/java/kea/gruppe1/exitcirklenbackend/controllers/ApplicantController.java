package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.DTO.ApplicantDTO;
import kea.gruppe1.exitcirklenbackend.email.EmailService;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.models.SurveyResult;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import kea.gruppe1.exitcirklenbackend.repositories.SurveyResultRepository;
import kea.gruppe1.exitcirklenbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ApplicantController {

    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    SurveyResultRepository surveyResultRepository;

    EmailService emailService;

    /**
     * Gets all applicants in the database
     */
    @GetMapping("/applicants")
    @PreAuthorize("isAuthenticated()")
    public List<Applicant> getApplicants() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(principal.getCity());
        if (authentication.getAuthorities().toArray()[0].toString().equals(EmployeeResponsibility.VISITATOR.name())) {
            System.out.println("VISITATOR");
            List<Applicant> applicants = applicantRepository.findApplicantByStatusIn(Arrays.asList(ApplicantStatus.IKKE_VISITERET, ApplicantStatus.I_PROCESS));
            System.out.println(applicants);
            return applicants;
        }
        if (authentication.getAuthorities().toArray()[0].toString().equals(EmployeeResponsibility.GRUPPEANSVARLIGE.name())) {
            System.out.println("GROUP");
            return applicantRepository.findApplicantByStatusIn(Arrays.asList(ApplicantStatus.VISITERET));
        }
        System.out.println("ADMIN");
        return applicantRepository.findAll();
    }

    /**
     * Get a single applicant from id
     *
     * @param id The id from the applicant
     * @return Applicant with provided id or return null
     */
    @GetMapping("/applicants/{id}")
    public Applicant getApplicant(@PathVariable Long id) {
        return applicantRepository.findById(id).orElse(null);
    }

    /**
     * Gets all applicants from a specific status
     *
     * @param status is the wanted status
     * @return A list of all applicants with the status in the parameter
     */
    @GetMapping("/applicants/status/{status}")
    public List<Applicant> getApplicantsByStatus(@PathVariable ApplicantStatus status) {
        return applicantRepository.findApplicantByStatus(status);
    }


    /**
     * Gets all applicants with the specified paid status and status
     *
     * @return a list of all applicants with the provided paid status and status
     */
    @GetMapping("/applicants/waiting-list")
    public List<Applicant> getApplicantsByPaidStatusAndStatus() {
        return applicantRepository.findApplicantByPaidStatusAndStatus(true, ApplicantStatus.VISITERET);
    }

    /**
     * Gets all applicants from a specified city
     *
     * @param city is the wanted city
     * @return A list of all applicants with the specified city
     */
    @GetMapping("/applicants/by/{city}")
    public List<Applicant> getApplicantsByCity(@PathVariable String city) {
        return applicantRepository.findApplicantByCity(city);
    }

    /**
     * @return a list of all status from the ApplicantStatus enum class
     */
    @GetMapping("/applicants/status")
    public List<ApplicantStatus> getApplicantsStatus() {
        return new ArrayList<>(Arrays.asList(ApplicantStatus.values()));
    }

    @PostMapping("/applicants")
    public Applicant createApplicant(@RequestBody Applicant newApplicant) {
        if (newApplicant.getUserType() == 1) {
            newApplicant.setStatus(ApplicantStatus.IKKE_VISITERET);
        } else {
            newApplicant.setStatus(null);
            newApplicant.setLastChanged(null);
        }
        emailService.sendWelcomeEmail(newApplicant);
        return applicantRepository.save(newApplicant);
    }

    /**
     * Updates only the changed values on the requested applicant
     *
     * @param id                the specific applicants id
     * @param applicantToUpdate the applicant object which needs to be updated
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
    @PatchMapping("/applicants/{id}")
    public HttpStatus updateApplicants(@PathVariable Long id,
                                       @RequestBody Applicant applicantToUpdate) {

        applicantRepository.findById(id).map(applicant -> {
            if (applicantToUpdate.getName() != null) applicant.setName(applicantToUpdate.getName());
            if (applicantToUpdate.getAge() != null) applicant.setAge(applicantToUpdate.getAge());
            if (applicantToUpdate.getGender() != null) applicant.setGender(applicantToUpdate.getGender());
            if (applicantToUpdate.getEmail() != null) applicant.setEmail(applicantToUpdate.getEmail());
            if (applicantToUpdate.getPhoneNumber() != null)
                applicant.setPhoneNumber(applicantToUpdate.getPhoneNumber());
            if (applicantToUpdate.getCity() != null) applicant.setCity(applicantToUpdate.getCity());
            if (applicantToUpdate.getLastChanged() != null)
                applicant.setLastChanged(applicantToUpdate.getLastChanged().truncatedTo(ChronoUnit.SECONDS));
            if (applicantToUpdate.getStatus() != null && !applicantToUpdate.getStatus().equals(applicant.getStatus())) {
                applicant.setStatus(applicantToUpdate.getStatus());
                applicant.setLastChanged(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            }
            if (applicantToUpdate.getDescription() != null)
                applicant.setDescription(applicantToUpdate.getDescription());
            if (applicantToUpdate.getPriority() != 0) applicant.setPriority(applicantToUpdate.getPriority());

            applicant.setContactCall(applicantToUpdate.isContactCall());
            applicant.setContactText(applicantToUpdate.isContactText());

            applicantRepository.save(applicant);
            return HttpStatus.OK;
        });
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Delete a specific applicant
     *
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

    @PostMapping("/applicants/{id}/survey")
    public HttpStatus completeSurvey(@PathVariable Long id, @RequestBody SurveyResult surveyResult) {
        try {
            Applicant applicant = applicantRepository.findById(id).get();
            applicant.setAnsweredSurvey(true);
            surveyResultRepository.save(surveyResult);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/applicants/visitation-request")
    public HttpStatus visitationRequest(@RequestBody ApplicantDTO applicantDTO) {
        try {
            System.out.println(emailService);
            emailService.sendVisitationOfferEmail(applicantDTO.getApplicant(), applicantDTO.getTime());
            applicantDTO.getApplicant().setStatus(ApplicantStatus.I_PROCESS);
            applicantDTO.getApplicant().setLastChanged(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            applicantRepository.save(applicantDTO.getApplicant());
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/applicants/cancel-visitation")
    public HttpStatus cancelVisitation(@RequestBody ApplicantDTO applicantDTO) {
        try {
            emailService.sendCancelVisitationEmail(applicantDTO.getApplicant(), applicantDTO.getReason());
            applicantDTO.getApplicant().setStatus(ApplicantStatus.IKKE_VISITERET);
            applicantDTO.getApplicant().setLastChanged(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            applicantRepository.save(applicantDTO.getApplicant());
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/applicants/confirm-visitation")
    public HttpStatus confirmVisitation(@RequestBody Applicant applicant) {
        try {
            emailService.sendConfirmationVisitationEmail(applicant);
            applicant.setStatus(ApplicantStatus.VISITERET);
            applicant.setLastChanged(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            applicantRepository.save(applicant);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;

    }

}
