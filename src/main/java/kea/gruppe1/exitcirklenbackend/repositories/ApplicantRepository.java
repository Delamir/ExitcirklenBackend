package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    /**
     * Finds all applicants by a specific status
     * @param status the wanted status to search for
     * @return a list of all applicants with the specified status
     */
    List<Applicant> findApplicantByStatus(ApplicantStatus status);

    /**
     * Find all applicants in multiple statuses
     * @param statuses the list of specified statues
     * @return a list of applicants
     */
    List<Applicant> findApplicantByStatusIn(Collection<ApplicantStatus> statuses);

    /**
     * Finds all applicants by a specific city
     * @param city the wanted city to search for
     * @return a list of all applicants with the specified city
     */
    List<Applicant> findApplicantByCity(City city);

    /**
     * Finds all applicants by their paid status and their status
     * @param paidStatus is the paid status either true or false
     * @param status the status of an applicant
     * @return a list of applicants with specified paid status and status
     */
    List<Applicant> findApplicantByPaidStatusAndStatus(boolean paidStatus, ApplicantStatus status);

    /**
     * Finds all applicants by their paid status, their status and their city
     * @param paidStatus is the paid status either true or false
     * @param status the status of an applicant
     * @param city the specified city
     * @return a list of applicants
     */
    List<Applicant> findApplicantByPaidStatusAndStatusAndCity(boolean paidStatus, ApplicantStatus status, City city);
}
