package mk.ukim.finki.wp.fcseservices.web.dosie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.disciplinary.*;
import mk.ukim.finki.wp.fcseservices.model.exceptions.DisciplinaryMeetingNotFound;
import mk.ukim.finki.wp.fcseservices.model.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.wp.fcseservices.model.exceptions.StudentNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.DisciplinaryMeetingParticipantRepository;
import mk.ukim.finki.wp.fcseservices.service.DisciplinaryRecordService;
import mk.ukim.finki.wp.fcseservices.service.MeetingService;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import mk.ukim.finki.wp.fcseservices.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final DisciplinaryMeetingParticipantRepository participantRepository;
    private final DisciplinaryRecordService disciplinaryRecordService;

    public boolean errorThrown = false;
    public boolean errorThrownOnUpdate = false;

    @GetMapping("/meetings")
    public String getMeetings(@RequestParam(name = "professor", required = false) String professor,
                              @RequestParam(name = "date", required = false) LocalDate date,
                              @RequestParam(name = "record", required = false) Long record,
                              @RequestParam(defaultValue = "0") int page,
                              Model model, HttpServletRequest request) {

        int pageSize = 10;

        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("records", disciplinaryRecordService.findAll());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        try{
            Page<DisciplinaryMeeting> meetingsPage = this.meetingService.findAllMeetings(professor, date, record, page, pageSize);
            //model.addAttribute("allMeetings", meetingsPage.getContent());
            model.addAttribute("meetingsPage", meetingsPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", meetingsPage.getTotalPages());
            model.addAttribute("previousPage", page - 1);
            model.addAttribute("nextPage", page + 1);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 0; i < meetingsPage.getTotalPages(); i++) {
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        } catch (ProfessorNotFoundException e) {
            model.addAttribute("errorMessage", "Professor not found.");
        }

        /*List<DisciplinaryMeeting> meetings = meetingService.getAllMeetings();
        model.addAttribute("allMeetings", meetings);*/

        return "dosie/meetings";
    }

    @GetMapping("/meeting/add-form")
    public String showMeetingForm(@RequestParam(value = "id", required = false) Long id,
                                  Model model, HttpServletRequest request) throws DisciplinaryMeetingNotFound{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (id != null) {
            DisciplinaryMeeting meeting = meetingService.getMeetingById(id);
            model.addAttribute("meeting", meeting);
        } else {
            model.addAttribute("meeting", null);
        }
        List<Professor> professors = professorService.findAll();
        List<Student> reportedStudents = studentService.findStudentByStatus(DisciplinaryStatus.REPORTED);

        model.addAttribute("username", username);
        model.addAttribute("professors", professors);
        model.addAttribute("students", reportedStudents);

        return "dosie/meeting-form";
    }

    @PostMapping("/meeting/add/")
    public String addMeeting(@RequestParam String meetingDate,
                             @RequestParam String professors,
                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response) throws StudentNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean errorThrown = false;
        try {
            DisciplinaryMeeting meeting = meetingService.createNewMeeting(meetingDate);

            if (professors != null && !professors.isEmpty()) {
                meetingService.addParticipants(professors, meeting);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("professorsError", "Participants are required.");
                errorThrown = true;
                return "dosie/meeting-form";
            }

            /*if (students != null && !students.isEmpty()) {
                meetingService.addParticipants(students, meeting);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("studentsError", "Participants are required.");
                errorThrown = true;
                return "dosie/meeting-form";
            }*/

            this.disciplinaryRecordService.setMeetingToRecordsWithoutMeeting(meeting);
        } catch (ProfessorNotFoundException professorNotFoundException) {
            model.addAttribute("username", username);
            model.addAttribute("professorError", professorNotFoundException.getMessage());

            errorThrown = true;

            //return "dosie/meeting-form";
            return "redirect:/meetings";
        }

        if (!errorThrown) {
            model.addAttribute("username", username);
            model.addAttribute("meetingCreated", true);

            return "dosie/meeting-form";
        }

        return "redirect:/meetings";
    }

    @Transactional
    @PostMapping("/meeting/add/{id}")
    public String editMeeting(
            @PathVariable Long id,
            @RequestParam(name = "meetingDate") String meetingDate,
            @RequestParam(name="students", required = false) List<String> studentIds,
            @RequestParam(name = "professors") List<String> professorIds,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) throws DisciplinaryMeetingNotFound, StudentNotFoundException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        errorThrownOnUpdate = false;

        try {
            DisciplinaryMeeting meeting = meetingService.editMeeting(id, meetingDate, professorIds);
            model.addAttribute("username", username);
            model.addAttribute("date", meeting.getDisciplinaryMeetingDate().toString());
            model.addAttribute("selectedStudents", studentIds);
            model.addAttribute("selectedProfessors", professorIds);
            model.addAttribute("updateSuccessfull", true);

            response.setHeader("Refresh", "2;url=/meetings");
            return "dosie/meeting-form";
        } catch (ProfessorNotFoundException e) {
            errorThrownOnUpdate = true;
            DisciplinaryMeeting meeting = meetingService.getMeetingById(id);
            model.addAttribute("username", username);
            model.addAttribute("date", meeting.getDisciplinaryMeetingDate().toString());
            model.addAttribute("selectedStudents", studentIds);
            model.addAttribute("selectedProfessors", professorIds);
            model.addAttribute("professorError", e.getMessage());

            return "dosie/meeting-form";
        }
    }

    @GetMapping("/meeting/edit/{id}")
    public String showEdit(@PathVariable Long id,
                           Model model) throws DisciplinaryMeetingNotFound {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        DisciplinaryMeeting meeting = meetingService.getMeetingById(id);
        List<DisciplinaryRecord> records = disciplinaryRecordService.findAll().stream()
                .filter(record -> record.getMeeting() == meeting).toList();
        List<Professor> professors = professorService.findAll();

        List<DisciplinaryMeetingParticipant> participants = participantRepository.findAllByMeeting(meeting);

       List<Student> students = studentService.findAllStudents();
       List<String> selectedStudents = new ArrayList<>();
       for(DisciplinaryRecord record : records){
           selectedStudents.add(record.getStudent().getIndex());
       }

        List<String> selectedProfessors = participants.stream()
                .filter(participant -> participant.getProfessor() != null)
                .map(participant -> participant.getProfessor().getId())
                .collect(Collectors.toList());

        model.addAttribute("meeting", meeting);
        model.addAttribute("date", meeting.getDisciplinaryMeetingDate().toString());
        model.addAttribute("students", students);
        model.addAttribute("professors", professors);
        model.addAttribute("selectedStudents", selectedStudents);
        model.addAttribute("selectedProfessors", selectedProfessors);
        model.addAttribute("username", username);

        return "dosie/meeting-form";
    }
}
