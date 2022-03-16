package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class DisciplineFactory implements AbstractFactoryForRelatedResources<Discipline, DisciplineDTO> {

    public Discipline create(DisciplineDTO disciplineDTO) {
        Discipline discipline = new Discipline();
        discipline.setName(disciplineDTO.getName());
        discipline.setIdFromSource(disciplineDTO.getId());
        //discipline.setStatus(Status.valueOf(disciplineDTO.getStatus()));
        return discipline;
    }
}
