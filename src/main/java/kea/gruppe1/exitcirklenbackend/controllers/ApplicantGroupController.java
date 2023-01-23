package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.services.EmailService;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.models.City;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import kea.gruppe1.exitcirklenbackend.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('GRUPPEANSVARLIG')")
public class ApplicantGroupController {

    @Autowired
    ApplicantGroupRepository applicantGroupRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    EmailService emailService;

    /**
     * Gets all groups in the database
     */
    @GetMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public List<ApplicantGroup> getGroups() {
        return applicantGroupRepository.findAll();
    }

    /**
     * Get a single group from id
     * @param id The id from the group
     * @return group with provided id or return null
     */
    @GetMapping("/groups/{id}")
    public ApplicantGroup getGroupById(@PathVariable Long id) {
        return applicantGroupRepository.findById(id).orElse(null);
    }

    /**
     * Get the available spots in a group
     * @param id the specific group
     * @return the remaining numbers of spots left in a group
     */
    @GetMapping("/groups/{id}/availableCount")
    public int availableCount (@PathVariable Long id) {
        ApplicantGroup group = applicantGroupRepository.findById(id).get();
        int applicantCount = group.getInviteList().size();
        return group.getGroupSize() - applicantCount;
    }

    /**
     * Get all the groups from the specified city
     * @param cityId the id from the wanted city
     * @return a list of applicant groups
     */
    @GetMapping("/groups/by/{cityId}")
    public List<ApplicantGroup> getApplicantGroupByCity(@PathVariable Long cityId) {
        City city = cityRepository.findById(cityId).get();
        return applicantGroupRepository.findApplicantGroupByCity(city);
    }

    /**
     * Saves a new group to the database
     * @param newGroup the group data that is saved
     * @return the saved group
     */
    @PostMapping("/groups")
    public ApplicantGroup addGroup (@RequestBody ApplicantGroup newGroup) {
        return applicantGroupRepository.save(newGroup);
    }

    /**
     * Replaces data in the database with a new applicant group
     * @param id the applicant group id
     * @param applicantGroupToUpdateWith the updated data of the applicant group
     * @return a string with a status
     */
    @PutMapping("/groups/{id}")
    public String updateApplicantGroupById(@PathVariable Long id, @RequestBody ApplicantGroup applicantGroupToUpdateWith) {
        if (applicantGroupRepository.existsById(id)) {
            if(!applicantGroupToUpdateWith.getId().equals(id)) {
                applicantGroupRepository.deleteById(id);
            }
            applicantGroupRepository.save(applicantGroupToUpdateWith);
            return "Group was created";
        } else {
            return "Group not found";
        }
    }

    /**
     * Updates data in the database of the specified applicant group
     * @param id the applicant group id
     * @param applicantGroupToUpdateWith the updated data of the applicant group
     * @return a string with a status
     */
    @PatchMapping("/groups/{id}")
    public String PatchApplicantGroupById(@PathVariable Long id, @RequestBody ApplicantGroup applicantGroupToUpdateWith) {
         applicantGroupRepository.findById(id).map(foundApplicantGroup -> {
            if (applicantGroupToUpdateWith.getCity() != null)
                foundApplicantGroup.setCity(applicantGroupToUpdateWith.getCity());
            if (applicantGroupToUpdateWith.getName() != null)
                foundApplicantGroup.setName(applicantGroupToUpdateWith.getName());
            if (applicantGroupToUpdateWith.getAddress() != null)
                foundApplicantGroup.setAddress(applicantGroupToUpdateWith.getAddress());
            if (applicantGroupToUpdateWith.getStartDate() != null)
                foundApplicantGroup.setStartDate(applicantGroupToUpdateWith.getStartDate());

            if (applicantGroupToUpdateWith.getGroupSize() != 0)
                foundApplicantGroup.setGroupSize(applicantGroupToUpdateWith.getGroupSize());
            if (applicantGroupToUpdateWith.getPrice() != 0)
                foundApplicantGroup.setPrice(applicantGroupToUpdateWith.getPrice());

            applicantGroupRepository.save(foundApplicantGroup);
            return "Applicant group was updated";
        });
        return "Applicant group was not found";
    }

    /**
     * Deletes a specific applicant group
     * @param id of the group that needs to be deleted
     */
    @DeleteMapping("/groups/{id}")
        public void deleteApplicantGroupById(@PathVariable Long id) {
        applicantGroupRepository.deleteById(id);
    }

    /**
     * Sends a group invite to all applicant group memebers
     * @param id the specific applicant group
     * @param ids a list of ids of the applicants in the group
     */
    @PostMapping("/groups/{id}/send-invites")
    public void sendInvites(@PathVariable Long id, @RequestBody List<Long> ids){
        ApplicantGroup group = applicantGroupRepository.getReferenceById(id);
        for(Long appId : ids){
            Applicant applicant = applicantRepository.getReferenceById(appId);
            group.addToInviteList(applicant);
            applicant.setStatus(ApplicantStatus.INVITERET);
            applicantRepository.save(applicant);

        }
        emailService.sendInvitations(group, group.getInviteList());
        applicantGroupRepository.save(group);
    }
}
