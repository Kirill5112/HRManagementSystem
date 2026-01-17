package isys.labs.staff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Локальный кэш должностей из сервиса 2.
 */
@Entity
@Table(name = "position_cache")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Id
    private Long id; // ID из Service 2

    @Column(nullable = false)
    private String name;

    @Column
    private String code;

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    @UpdateTimestamp
    private LocalDateTime syncedAt;
}