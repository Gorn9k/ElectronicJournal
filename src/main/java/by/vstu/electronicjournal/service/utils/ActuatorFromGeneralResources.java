package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface ActuatorFromGeneralResources<D extends AbstractDTO> {

    RestTemplate restTemplate = new RestTemplate();

    List<D> findAndAddThings(String pathToRelatedResources);

}
