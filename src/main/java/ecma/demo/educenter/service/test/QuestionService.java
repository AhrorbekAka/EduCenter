package ecma.demo.educenter.service.test;

import ecma.demo.educenter.behavior.Creatable;
import ecma.demo.educenter.behavior.ListCreatable;
import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.entity.attachment.ImageModel;
import ecma.demo.educenter.entity.test.Answer;
import ecma.demo.educenter.entity.test.Question;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.Request;
import ecma.demo.educenter.payload.test.ReqAnswer;
import ecma.demo.educenter.payload.test.ReqQuestion;
import ecma.demo.educenter.projections.ResAnswer;
import ecma.demo.educenter.projections.ResQuestion;
import ecma.demo.educenter.projections.ResQuestionInterf;
import ecma.demo.educenter.repository.SubjectRepository;
import ecma.demo.educenter.repository.test.AnswerRepository;
import ecma.demo.educenter.repository.test.QuestionRepository;
import ecma.demo.educenter.service.attachment.ImageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService implements Creatable, ListCreatable, Readable {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SubjectRepository subjectRepository;

    private final ImageService imageService;

    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, SubjectRepository subjectRepository, ImageService imageService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.subjectRepository = subjectRepository;
        this.imageService = imageService;
    }

    @Override
    public ApiResponse createAll(List<?> request) {
        saveQuestionList((List<ReqQuestion>) request);
        return new ApiResponse("All saved", true);
    }

    List<Question> saveQuestionList(List<ReqQuestion> reqQuestionList) {
        List<Question> resList = new ArrayList<>();
        for (ReqQuestion reqQuestion : reqQuestionList) {
            resList.add(saveQuestionWithAnswers(reqQuestion));
        }
        return resList;
    }

    public Question saveQuestionWithAnswers(ReqQuestion reqQuestion) {
        try {
            if (reqQuestion.getAnswers().size() == 4) {
//                final ImageModel img = imageService.saveImage(reqQuestion.getFile());
                final Subject subject = subjectRepository.findBySubjectName(reqQuestion.getSubjectName());
                final Question question = questionRepository.save(new Question(reqQuestion.getQuestion(), subject));

                saveAnswerList(reqQuestion.getAnswers(), question);
                return question;
            } else {
                return new Question();
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void saveAnswerList(List<ReqAnswer> reqAnswerList, Question question) {
        int counter = 0;
        for (ReqAnswer reqAnswer : reqAnswerList) {
            if (reqAnswer.getIsCorrect()) counter++;
            if (counter > 1) {
                --counter;
                break;
            }
            saveAnswer(reqAnswer, question);
        }
    }

    private void saveAnswer(ReqAnswer reqAnswer, Question question) {
        try {
            answerRepository.save(new Answer(reqAnswer.getAnswer(), reqAnswer.getIsCorrect(), question));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ApiResponse create(Request request) {
        Question question = saveQuestionWithAnswers((ReqQuestion) request);
        if (!question.equals(new Question())) {
            return new ApiResponse("Question saved", true);
        }
        return new ApiResponse("Error", false);
    }

    List<UUID> checkAnswers(List<UUID> idList) {
        List<UUID> resIdList = new ArrayList<>();
        for (UUID id : idList) {
            try {
                Optional<Answer> optionalAnswer = answerRepository.findById(id);
                optionalAnswer.ifPresent(answer -> {
                    if (optionalAnswer.get().isCorrect()) resIdList.add(id);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resIdList;
    }

    @Override
    public ApiResponse read(User user, Object request) {
        if(request instanceof UUID) {
            return getTestQuestions((UUID) request);
        }
        return null;
    }

    private ApiResponse getTestQuestions(UUID testId) {
        try {
            List<ResQuestion> resQuestionList= new ArrayList<>();
            List<ResQuestionInterf> questionList = questionRepository.findAllByTestId(testId);
            for (ResQuestionInterf resQuestion : questionList) {
                UUID id = resQuestion.getId();
                List<ResAnswer> resAnswerList = answerRepository.findAllByQuestionId(id);
                resQuestionList.add(new ResQuestion(resQuestion.getId(), resQuestion.getQuestion(), resAnswerList));
            }
            return new ApiResponse("Question list by test id", true, resQuestionList);
        } catch (Exception e){
            return new ApiResponse("Error", false);
        }
    }
}