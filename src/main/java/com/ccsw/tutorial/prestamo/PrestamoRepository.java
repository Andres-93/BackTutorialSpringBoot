package com.ccsw.tutorial.prestamo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ccsw.tutorial.prestamo.model.Prestamo;

public interface PrestamoRepository extends CrudRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = { "game", "client" })
    List<Prestamo> findAll(Specification<Prestamo> spec);

    @Query(value = "select * from prestamo p where game_id = ?1 and client_id = ?2", nativeQuery = true)
    List<Prestamo> findByGameIdAndClientId(Long gameId, Long clientId);

    @Query(value = "select * from prestamo p where client_id = ?1", nativeQuery = true)
    List<Prestamo> findByClientId(Long clientId);

    @Query(value = "select * from prestamo p where game_id = ?1", nativeQuery = true)
    List<Prestamo> findByGameId(Long gameId);

}
