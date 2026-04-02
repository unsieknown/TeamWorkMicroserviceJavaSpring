package com.mordiniaa.teamservice.repositories;

import com.mordiniaa.teamservice.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    boolean existsTeamByTeamIdAndManagerId(UUID teamId, UUID managerId);

    @Query("select count(t) > 0 from Team t where t.teamId = :teamId and :userId member of t.teamMembers")
    boolean existsUserInTeam(UUID teamId, UUID userId);

    boolean existsByTeamNameIgnoreCase(String teamName);

    List<Team> findAllByManagerId(UUID managerUserId);

    Optional<Team> findTeamByTeamIdAndManagerId(UUID teamId, UUID managerUserId);

    Optional<Team> findTeamByTeamIdAndActiveTrue(UUID teamId);

    @Modifying
    @Query("""
            update Team t
            set t.managerId = null
            where t.managerId = :managerId
            """)
    void removeManagerFromAllTeams(UUID managerId);

    @Modifying
    @Query(value = "DELETE FROM teams_users WHERE user_id = :userId", nativeQuery = true)
    void removeMemerFromAllTeams(UUID userId);

    boolean existsByTeamNameIgnoreCaseAndActiveTrue(String teamName);
}
