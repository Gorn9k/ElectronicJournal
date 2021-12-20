package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("search")
    public List<GroupDTO> search(@RequestParam("q") String query) {
        return groupService.search(query);
    }
}
