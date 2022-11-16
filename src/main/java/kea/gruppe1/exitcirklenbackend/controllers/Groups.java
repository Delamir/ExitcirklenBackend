package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Group;
import kea.gruppe1.exitcirklenbackend.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Groups {

    @Autowired
    GroupRepository groupRepository;

    /**
     *
     * Gets all groups in the database
     */
    @GetMapping("/groups")
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    /**
     * Get a single group from id
     * @param id The id from the group
     * @return group with provided id or return null
     */
    @GetMapping("/groups/{id}")
    public Group getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id).get();
    }

    @PostMapping("/groups")
    public Group addGroup (@RequestBody Group newGroup) {
        return groupRepository.save(newGroup);
    }

    @PutMapping("/groups/{id}")
    public String updateGroupById(@PathVariable Long id, @RequestBody Group groupToUpdateWith) {
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
