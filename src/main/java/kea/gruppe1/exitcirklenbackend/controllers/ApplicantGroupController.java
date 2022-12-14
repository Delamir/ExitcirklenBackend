package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.email.EmailService;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/groups/{id}/availableCount")
    public int availableCount (@PathVariable Long id) {
        ApplicantGroup group = applicantGroupRepository.findById(id).get();
        int applicantCount = group.getInviteList().size();
        return group.getGroupSize() - applicantCount;
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
