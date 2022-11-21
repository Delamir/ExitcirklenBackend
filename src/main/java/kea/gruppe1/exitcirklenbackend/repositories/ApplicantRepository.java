package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    /**
     * Finds all applicant by a specific status
     * @param status the wanted status to search for
     * @return a list of all applicants with the specified status
     */
    List<Applicant> findApplicantByStatus(ApplicantStatus status);

    /**
     * Finds all applicants by a specific city
     * @param city the wanted city to search for
     * @return a list of all applicants with the specified city
     */
    List<Applicant> findApplicantByCity(String city);

    /**
     * Finds all applicants by their paid status and their status
     * @param paidStatus is the paid status either true or false
     * @param Status the status of an applicant
     * @return a list of applicants with specified paid status and status
     */
    List<Applicant> findApplicantByPaidStatusAndStatus(boolean paidStatus, ApplicantStatus status);
}
