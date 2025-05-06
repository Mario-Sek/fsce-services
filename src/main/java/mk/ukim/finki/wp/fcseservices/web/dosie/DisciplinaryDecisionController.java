package mk.ukim.finki.wp.fcseservices.web.dosie;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinaryRecord;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.DisciplinarySanction;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryRecordNotFoundException;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryDecisionService;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryRecordService;
import mk.ukim.finki.wp.fcseservices.service.DisciplinarySanctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/disciplinary")
@RequiredArgsConstructor
public class DisciplinaryDecisionController {

    private final DisciplinaryRecordService disciplinaryRecordService;
    private final DisciplinarySanctionService disciplinarySanctionService;
    private final DisciplinaryDecisionService disciplinaryDecisionService;

    @GetMapping("/{recordId}/decision")
    public String showDecisionForm(@PathVariable String recordId, Model model) {
        try {
            DisciplinaryRecord record = disciplinaryRecordService.findReportById(recordId);
            List<DisciplinarySanction> sanctions = disciplinarySanctionService.findAllSanctions();

            model.addAttribute("record", record);
            model.addAttribute("sanctions", sanctions);
            model.addAttribute("decision", record.getDecision());

            return "dosie/decision-form";
        } catch (DisciplinaryRecordNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{recordId}/decision")
    public String saveDecision(
            @PathVariable String recordId,
            @RequestParam(name = "sanctionId") Long sanctionId,
            @RequestParam(name = "description", required = false) String description,
            Model model) {

        try {
            DisciplinaryRecord record = disciplinaryDecisionService.createOrUpdateDecision(recordId, sanctionId, description);
            model.addAttribute("successMessage", "Decision saved successfully");
            model.addAttribute("record", record);
            model.addAttribute("decision", record.getDecision());
            model.addAttribute("sanctions", disciplinarySanctionService.findAllSanctions());

            return "dosie/decision-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
