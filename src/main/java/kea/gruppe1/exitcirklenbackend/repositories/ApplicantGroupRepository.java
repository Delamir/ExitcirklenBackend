package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantGroupRepository extends JpaRepository<ApplicantGroup, Long> {

    List<ApplicantGroup> findApplicantGroupByCity(City city);
}
