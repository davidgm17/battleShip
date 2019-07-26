package com.formacion.battleship.service.converters;

import com.formacion.models.database.Flota;
import com.formacion.models.request.FlotaRequestJto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Gomez on 22/07/2019.
 * Arteco Consulting Sl
 * mailto: info@arteco-consulting.com
 */
@Service
public class FlotaConverter {

    public List<Flota> getFlota(List<FlotaRequestJto> flotaInput) {
        return flotaInput.stream()
                .map(Flota::new)
                .collect(Collectors.toList());
    }

}
