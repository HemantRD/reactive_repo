package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.IdpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on
 * {@link IdpEntity} using Spring Data JPA.
 * <p>
 * Provides built-in methods for persistence and query derivation
 * (e.g., findById, save, delete, findAll) and includes custom
 * lookups for workflow-based access.
 * </p>
 */
@Repository
public interface IIdpRepository extends JpaRepository<IdpEntity, Long> {

    /**
     * Finds an IDP by its active workflow identifier.
     *
     * @param activeFlowId the active workflow ID
     * @return optional {@link IdpEntity} if found
     */
    Optional<IdpEntity> findByActiveFlowId(Long activeFlowId);
}
