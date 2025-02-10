package mk.ukim.finki.wp.fcseservices.model.dto;

import lombok.Data;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.model.base.StudyProgram;
import mk.ukim.finki.wp.fcseservices.model.accreditations.SubjectDetails;

import java.util.List;

@Data
public class SubjectInfoDto {

    public SubjectDetails subject;

    public List<Professor> professors;

    public List<StudyProgram> mandatoryStudyPrograms;

    public List<StudyProgram> electiveStudyPrograms;
}
