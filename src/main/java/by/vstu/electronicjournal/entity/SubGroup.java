package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sub_group")
@AttributeOverride(name = "id", column = @Column(name = "sub_group_id"))
public class SubGroup extends AbstractEntity {

    @Column(name = "sub_group_number")
    private Byte subGroupNumber;

}
