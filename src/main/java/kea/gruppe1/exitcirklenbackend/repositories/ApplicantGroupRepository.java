package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantGroupRepository extends JpaRepository<ApplicantGroup, Long> {

    /**
     * Finds all applicant groups from the specified city
     * @param city the specific city
     * @return a list of applicant groups
     */
    List<ApplicantGroup> findApplicantGroupByCity(City city);
}
