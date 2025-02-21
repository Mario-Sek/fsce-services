package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.SubjectNeedsView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectNeedsService {
    Page<SubjectNeedsView> list(String semesterCode, String name,
                                int page,
                                int results,
                                Boolean lessThanPRG,
                                Boolean lessThanPR,
                                Boolean lessThanARG,
                                Boolean lessThanAR,
                                Boolean lessThanCL
    );

    List<SubjectNeedsView> findAllForExport(String semesterCode,
                                            String name,
                                            Boolean lessThanPRG,
                                            Boolean lessThanPR,
                                            Boolean lessThanARG,
                                            Boolean lessThanAR,
                                            Boolean lessThanCL
    );
}
