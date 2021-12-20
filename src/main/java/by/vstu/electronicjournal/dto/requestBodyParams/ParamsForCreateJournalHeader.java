package by.vstu.electronicjournal.dto.requestBodyParams;

import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import lombok.Data;

@Data
public class ParamsForCreateJournalHeader {

    private Long journalSiteId;
    private JournalHeaderDTO journalHeaderDTO;
}
