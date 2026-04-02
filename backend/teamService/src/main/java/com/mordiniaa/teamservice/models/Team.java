package com.mordiniaa.teamservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Team")
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(name = "uq_team_name", columnNames = "team_name")
})
public class Team extends BaseEntity {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID teamId;

    @Column(name = "team_name", nullable = false, length = 40)
    private String teamName;

    @Column(name = "presentation_name", nullable = false, length = 40)
    private String presentationName;

    @Column(name = "active")
    private boolean active = true;

    private UUID managerId;

    @ElementCollection
    @CollectionTable(name = "teams_users", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "user_id")
    private Set<UUID> teamMembers = new HashSet<>();

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public void removeManager() {
        this.managerId = null;
    }

    public void deactivate() {
        this.active = false;
    }

    public void addMember(UUID userId) {
        this.teamMembers.add(userId);
    }

    public void removeMember(UUID userId) {
        this.teamMembers.remove(userId);
    }
}
