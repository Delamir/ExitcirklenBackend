package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicantGroupControllerTest {

    @InjectMocks
    ApplicantGroupController applicantGroupController;

    @Mock
    ApplicantGroupRepository applicantGroupRepository;

    @Test
    void availableCount() {
        ApplicantGroup applicantGroup = new ApplicantGroup();
        applicantGroup.setId(1L);
        applicantGroup.setGroupSize(24);
        when(applicantGroupRepository.findById(1L)).thenReturn(Optional.of(applicantGroup));
        assertEquals(24, applicantGroupController.availableCount(1L));

        Applicant applicant = new Applicant();
        applicantGroup.addToInviteList(applicant);
        assertEquals(23, applicantGroupController.availableCount(1L));

        applicantGroup.removeFromInviteList(applicant);
        assertEquals(24, applicantGroupController.availableCount(1L));
    }
}
