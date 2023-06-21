package com.ccsw.tutorial.prestamo;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.IClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.ownException.ErrorNuevoPrestamoException;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrestamoServiceImpl implements IPrestamoService {

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    GameService gameService;

    @Autowired
    IClientService clientService;

    /*
     * @Override public List<Prestamo> findAll() { return (List<Prestamo>)
     * this.prestamoRepository.findAll(); }
     */
    @Override
    public Prestamo get(Long id) {
        return this.prestamoRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {

        PrestamoSpecification clientSpec = new PrestamoSpecification(
                new SearchCriteria("client.id", ":", dto.getIdClient()));

        PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", dto.getIdGame()));

        PrestamoSpecification fechaMayorIgual = new PrestamoSpecification(
                new SearchCriteria("startdate", "<", dto.getFecha()));
        PrestamoSpecification fechaMenorIgual = new PrestamoSpecification(
                new SearchCriteria("enddate", ">", dto.getFecha()));

        Specification<Prestamo> spec = Specification.where(fechaMenorIgual).and(fechaMayorIgual).and(clientSpec)
                .and(gameSpec);

        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, PrestamoDto dto) throws ErrorNuevoPrestamoException {

        // Evaluamos dias de prestamo
        String mensajeError = "";
        Long diasPrestamo = TimeUnit.DAYS.convert(dto.getEnddate().getTime() - dto.getStartdate().getTime(),
                TimeUnit.MILLISECONDS);
        if (diasPrestamo > 14) {
            mensajeError += "Error el maximo de dias del prestamo debe ser 14.";

        }

        // Lista de prestamos para un juego
        PrestamoSpecification gameSpec = new PrestamoSpecification(
                new SearchCriteria("game.id", ":", dto.getGame().getId()));

        PrestamoSpecification fechaMayorIgual = new PrestamoSpecification(
                new SearchCriteria("startdate", "<", dto.getEnddate()));
        PrestamoSpecification fechaMenorIgual = new PrestamoSpecification(
                new SearchCriteria("enddate", ">", dto.getStartdate()));

        Specification<Prestamo> spec = Specification.where(gameSpec).and(fechaMayorIgual).and(fechaMenorIgual);

        if (!this.prestamoRepository.findAll(spec).isEmpty()) {
            mensajeError += "Error el juego seleccionado no esta disponible los dias elegidos.";
        }

        // lista de prestamos de un cliente

        PrestamoSpecification clientSpec = new PrestamoSpecification(
                new SearchCriteria("client.id", ":", dto.getClient().getId()));

        spec = Specification.where(clientSpec).and(fechaMayorIgual).and(fechaMenorIgual);

        if (!this.prestamoRepository.findAll(spec).isEmpty()) {
            mensajeError += "Error ya tienes un juego prestado durante esos dias, por favor corrija las fechas.";
        }

        if (mensajeError != "") {
            throw new ErrorNuevoPrestamoException(mensajeError);
        }

        Prestamo prestamo = new Prestamo();

        BeanUtils.copyProperties(dto, prestamo, "id", "game", "client");

        prestamo.setClient(clientService.get(dto.getClient().getId()));
        prestamo.setGame(gameService.get(dto.getGame().getId()));

        this.prestamoRepository.save(prestamo);

    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.prestamoRepository.deleteById(id);

    }

}
