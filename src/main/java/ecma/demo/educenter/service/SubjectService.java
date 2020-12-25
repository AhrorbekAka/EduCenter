package ecma.demo.educenter.service;

import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.repository.SubjectRepository;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public ApiResponse getAll() {
        return new ApiResponse("All subjects", true, subjectRepository.findAll());
    }
}
