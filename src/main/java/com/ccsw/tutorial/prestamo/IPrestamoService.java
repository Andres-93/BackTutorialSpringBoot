package com.ccsw.tutorial.prestamo;

import org.springframework.data.domain.Page;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;

public interface IPrestamoService {

    // List<Prestamo> findAll();

    Prestamo get(Long id);

    Page<Prestamo> findPage(PrestamoSearchDto dto);

    void save(Long id, PrestamoDto dto);

    void delete(Long id) throws Exception;

}
