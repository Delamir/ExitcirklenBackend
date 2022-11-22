package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.email.EmailService;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicantGroupController {

    @Autowired
    ApplicantGroupRepository applicantGroupRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    EmailService emailService;

    /**
     * Gets all groups in the database
     */
    @GetMapping("/groups")
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

    @PostMapping("/groups")
    public ApplicantGroup addGroup (@RequestBody ApplicantGroup newGroup) {
        return applicantGroupRepository.save(newGroup);
    }

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

    @PatchMapping("/groups/{id}")
    public String PatchApplicantGroupById(@PathVariable Long id, @RequestBody ApplicantGroup applicantGroupToUpdateWith) {
        return applicantGroupRepository.findById(id).map(foundApplicantGroup -> {
            if (applicantGroupToUpdateWith.getCity() != null) foundApplicantGroup.setCity(applicantGroupToUpdateWith.getCity());
            if (applicantGroupToUpdateWith.getName() != null) foundApplicantGroup.setName(applicantGroupToUpdateWith.getName());
            if (applicantGroupToUpdateWith.getAddress() != null) foundApplicantGroup.setAddress(applicantGroupToUpdateWith.getAddress());
            if (applicantGroupToUpdateWith.getGroupSize() != null) foundApplicantGroup.setGroupSize(applicantGroupToUpdateWith.getGroupSize());
            if (applicantGroupToUpdateWith.getAvailableSpots() != null) foundApplicantGroup.setAvailableSpots(applicantGroupToUpdateWith.getAvailableSpots());
            if (applicantGroupToUpdateWith.getPrice() != null) foundApplicantGroup.setPrice(applicantGroupToUpdateWith.getPrice());
            if (applicantGroupToUpdateWith.getStartDate() != null) foundApplicantGroup.setStartDate(applicantGroupToUpdateWith.getStartDate());

            applicantGroupRepository.save(foundApplicantGroup);
            return "Applicant group was updated";
        }).orElse("Applicant group was not found");
    }

    @DeleteMapping("/groups/{id}")
        public void deleteApplicantGroupById(@PathVariable Long id) {
        applicantGroupRepository.deleteById(id);
    }

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
