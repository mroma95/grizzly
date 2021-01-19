package pl.mr.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.mr.model.Visit;
import pl.mr.service.VisitService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class VisitController {

    private VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE, value = "/{identifier}/{pin}")
    public void deleteVisit(@PathVariable(name = "identifier") int identifier, @PathVariable(name = "pin") int pin, @RequestBody @Valid Visit visit) {
        visitService.cancelVisit(identifier, pin, visit);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{identifier}/{pin}/{lastname}")
    public ResponseEntity<Visit> addVisit(@PathVariable(name = "identifier") int identifier, @PathVariable(name = "pin") int pin, @PathVariable(name = "lastname") String lastname, @RequestBody @Valid Visit visit) {
        ResponseEntity<Visit> visitResponseEntity = visitService.addVisit(identifier, pin, lastname, visit);
        return visitResponseEntity;
    }

    @RequestMapping(value = "/{lastname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Visit> getVisistsByDoctor(@PathVariable(name = "lastname") String lastname, @RequestBody @Valid Visit visit) {
        return visitService.getVisitsByDoctor(lastname, visit);
    }
}
