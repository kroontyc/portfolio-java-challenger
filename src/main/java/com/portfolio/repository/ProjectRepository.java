package com.portfolio.repository;

import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    Page<Project> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    Page<Project> findByFiltros(
            @Param("nome") String nome,
            @Param("status") ProjectStatus status,
            Pageable pageable
    );

    List<Project> findByGerenteId(Long gerenteId);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.membros LEFT JOIN FETCH p.gerente")
    List<Project> findAllWithMembros();

    @Query("SELECT COUNT(p) FROM Project p JOIN p.membros m " +
            "WHERE m.id = :membroId AND p.status NOT IN (:statusExcluidos)")
    long contarProjetosAtivosDoMembro(
            @Param("membroId") Long membroId,
            @Param("statusExcluidos") List<ProjectStatus> statusExcluidos
    );
}
