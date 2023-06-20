package com.ccsw.tutorial.prestamo;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;

public interface IPrestamoService {

    // List<Prestamo> findAll();

    Prestamo get(Long id);

    List<Prestamo> find(String title, Long idClient, String fecha);

    Page<Prestamo> findPage(PrestamoSearchDto dto);

    void save(Long id, PrestamoDto dto);

    void delete(Long id) throws Exception;

}
