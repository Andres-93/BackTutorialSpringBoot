package com.ccsw.tutorial.prestamo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
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

    @Override
    public List<Prestamo> findAll() {

        return (List<Prestamo>) this.prestamoRepository.findAll();
    }

    @Override
    public Prestamo get(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        return this.prestamoRepository.findAll(dto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, PrestamoDto dto) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Long id) throws Exception {
        // TODO Auto-generated method stub

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

            for (int i = 0; i < listaEncontrada.size(); i++) {

                try {
                    Date fechaAComparar = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
                    java.util.Date fechaInicio = new java.util.Date(listaEncontrada.get(i).getStartdate().getTime());
                    java.util.Date fechaFin = new java.util.Date(listaEncontrada.get(i).getEnddate().getTime());
                    if (fechaAComparar.compareTo(fechaInicio) < 0 || fechaAComparar.compareTo(fechaFin) > 0) {
                        listaEncontrada.remove(i);
                    }

                } catch (ParseException e) {

                    e.printStackTrace();
                }
            }

        }

        return listaEncontrada;
    }

}

/*
 * if (title == null && idClient == null) { return (List<Prestamo>)
 * this.prestamoRepository.findAll(); } else if (title == null) { return
 * this.prestamoRepository.findByClientId(idClient); }
 * 
 * Long idGame = null; Game game = this.gameService.find(title);
 * 
 * if (game != null) { idGame = game.getId(); } if (idClient == null) {
 * 
 * return this.prestamoRepository.findByGameId(idGame);
 * 
 * }
 * 
 * return this.prestamoRepository.findByGameIdAndClientId(idGame, idClient);
 */
