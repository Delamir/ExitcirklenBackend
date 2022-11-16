package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicantGroupController {

    @Autowired
    ApplicantGroupRepository groupRepository;

    /**
     *
     * Gets all groups in the database
     */
    @GetMapping("/groups")
    public List<ApplicantGroup> getGroups() {
        return groupRepository.findAll();
    }

    /**
     * Get a single group from id
     * @param id The id from the group
     * @return group with provided id or return null
     */
    @GetMapping("/groups/{id}")
    public ApplicantGroup getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id).get();
    }

    @PostMapping("/groups")
    public ApplicantGroup addGroup (@RequestBody ApplicantGroup newGroup) {
        return groupRepository.save(newGroup);
    }

    @PutMapping("/groups/{id}")
    public String updateGroupById(@PathVariable Long id, @RequestBody ApplicantGroup groupToUpdateWith) {
        if (groupRepository.existsById(id)) {
            if(!groupToUpdateWith.getId().equals(id)) {
                groupRepository.deleteById(id);
            }
            groupRepository.save(groupToUpdateWith);
            return "Group was created";
        } else {
            return "Group not found";
        }
    }

}
