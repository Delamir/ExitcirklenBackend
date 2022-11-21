package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    /**
     * Finds all applicant by a specific status
     * @param status the wanted status to search for
     * @return a list of all applicants with the specified status
     */
    List<Applicant> findApplicantByStatus(String status);

    /**
     * Finds all applicants by a specific city
     * @param city the wanted city to search for
     * @return a list of all applicants with the specified city
     */
    List<Applicant> findApplicantByCity(String city);

    /**
     * Finds all applicants by their paid status and their status
     * @param payedStatus is the paid status either true or false
     * @param Status
     * @return
     */
    List<Applicant> findApplicantByPayedStatusAndStatus(boolean payedStatus, String Status);
}
