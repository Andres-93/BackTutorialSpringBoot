package com.ccsw.tutorial.prestamo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.ccsw.tutorial.game.model.Game;
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

    @Override
    public List<Prestamo> findAll() {

        return (List<Prestamo>) this.prestamoRepository.findAll();
    }

    @Override
    public Prestamo get(Long id) {
        return this.prestamoRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        return this.prestamoRepository.findAll(dto.getPageable().getPageable());
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

        boolean continuarBusqueda = true;

        // Listado de los prestamos ya existentes de un juego.
        List<Prestamo> listaPrestamosGame = this.prestamoRepository.findByGameId(dto.getGame().getId());

        if (!listaPrestamosGame.isEmpty()) {
            for (int i = 0; i < listaPrestamosGame.size() && continuarBusqueda; i++) {

                Date fechaInicio = listaPrestamosGame.get(i).getStartdate();
                Date fechaFin = listaPrestamosGame.get(i).getEnddate();
                Date fechaDTO = dto.getStartdate();
                Date fechaFinDTO = dto.getEnddate();

                if ((fechaDTO.compareTo(fechaFin) <= 0 || fechaDTO.compareTo(fechaFin) == 0)
                        && (fechaInicio.compareTo(fechaFinDTO) <= 0 || fechaInicio.compareTo(fechaFinDTO) == 0)) {
                    mensajeError += "Error el juego seleccionado no esta disponible los dias elegidos.";
                    continuarBusqueda = false;
                }

            }
        }

        // lista de prestamos de un cliente
        List<Prestamo> listaPrestamosCliente = this.prestamoRepository.findByClientId(dto.getClient().getId());
        {
            continuarBusqueda = true;
            if (!listaPrestamosCliente.isEmpty()) {
                for (int i = 0; i < listaPrestamosCliente.size() && continuarBusqueda; i++) {
                    Date fechaInicio = listaPrestamosCliente.get(i).getStartdate();
                    Date fechaFin = listaPrestamosCliente.get(i).getEnddate();

                    Date fechaDTO = dto.getStartdate();
                    Date fechaFinDTO = dto.getEnddate();

                    if ((fechaDTO.compareTo(fechaFin) <= 0 || fechaDTO.compareTo(fechaFin) == 0)
                            && (fechaInicio.compareTo(fechaFinDTO) <= 0 || fechaInicio.compareTo(fechaFinDTO) == 0)) {
                        mensajeError += "Error ya tienes un juego prestado durante esos dias, por favor corrija las fechas.";
                        continuarBusqueda = false;
                    }

                }
            }
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

    @Override
    public List<Prestamo> find(String title, Long idClient, String fecha) {

        Game game = null;
        PrestamoSpecification titleSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", null));
        if (title != null && title != "") {
            game = this.gameService.find(title);
            if (game != null) {
                titleSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", game.getId()));
            } else {
                titleSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", 0));
            }
        }

        PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.id", ":", idClient));

        Specification<Prestamo> spec = Specification.where(titleSpec).and(clientSpec);

        List<Prestamo> listaEncontrada = this.prestamoRepository.findAll(spec);

        if (!listaEncontrada.isEmpty() && fecha != null) {

            List<Prestamo> listaFiltroFechas = new ArrayList<Prestamo>();

            for (int i = 0; i < listaEncontrada.size(); i++) {

                try {
                    Date fechaAComparar = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);

                    if ((fechaAComparar.compareTo(listaEncontrada.get(i).getStartdate()) > 0
                            && fechaAComparar.compareTo(listaEncontrada.get(i).getEnddate()) < 0)
                            || fechaAComparar.compareTo(listaEncontrada.get(i).getStartdate()) == 0
                            || fechaAComparar.compareTo(listaEncontrada.get(i).getEnddate()) == 0) {
                        listaFiltroFechas.add(listaEncontrada.get(i));
                    }

                } catch (ParseException e) {

                    e.printStackTrace();
                }
            }

            listaEncontrada = listaFiltroFechas;

        }

        return listaEncontrada;
    }

}
