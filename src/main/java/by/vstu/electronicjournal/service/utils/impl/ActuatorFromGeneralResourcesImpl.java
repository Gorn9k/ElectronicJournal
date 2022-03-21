package by.vstu.electronicjournal.service.utils.impl;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

public class ActuatorFromGeneralResourcesImpl<
        E extends AbstractEntity,
        D extends AbstractDTO,
        R extends RelationWithParentIdentifiers<E> & JpaRepository<E, Long>,
        F extends AbstractFactoryForRelatedResources,
        M extends Mapper>
        implements ActuatorFromGeneralResources {

    private Class<D> type;
    private R repository;
    private M mapper;
    private F factory;
    private boolean areAllAdded;

    public ActuatorFromGeneralResourcesImpl(Class<D> type, R repository, F factory, M mapper) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    public List<? extends D> findAndAddThings(String pathToRelatedResources) {
        String path = pathToRelatedResources.split("=")[0] + "=";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!areAllAdded) {
            if (sendRequestAndReturnResult(path, objectMapper).isEmpty()) {
                throw new IllegalArgumentException("Had no records by " + path);
            }

            areAllAdded = true;
        }

        List<? extends D> result = sendRequestAndReturnResult(pathToRelatedResources, objectMapper);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Had no records by " + pathToRelatedResources);
        }

        return result;
    }

    private List<? extends D> sendRequestAndReturnResult(String request, ObjectMapper objectMapper) {

        List<? super D> listResponseEntity = new ArrayList<>();

        try {
            restTemplate.exchange(request, HttpMethod.GET, null, new ParameterizedTypeReference<List<?>>() {
            })
                    .getBody()
                    .stream().forEach(entry -> {
                listResponseEntity.add(objectMapper.convertValue(entry, type));
            });
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Access error by address " + request);
        }

         return (List<? extends D>) searchAndCreate((List<? extends D>) listResponseEntity);
    }

    private List<?> searchAndCreate(List<? extends D> source) {

        List<? super D> result = new ArrayList<>();

        for (D dto : source) {
            D tempDTO = null;
            try {
                tempDTO = (D) mapper.toDTO(repository.findByIdFromSource(dto.getId()), type);
            } catch (IllegalArgumentException e) {
                System.err.println("Not found. " + dto + " will be add.");
            }

            if (tempDTO == null) {
                tempDTO = (D) mapper.toDTO(repository.save((E) factory.create(dto)), type);
            }
            result.add(tempDTO);
        }
        return result;
    }
}
