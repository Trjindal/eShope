package com.eshope.admin.Service;

import com.eShope.common.entity.Question;
import com.eShope.common.entity.Review;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    public static final int QUESTIONS_PER_PAGE = 5;

    public Page<Question> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,QUESTIONS_PER_PAGE,sort);

        if(keyword!=null){
            return questionRepository.findAll(keyword,pageable);
        }

        return questionRepository.findAll(pageable);
    }
    public Question getById(Integer id) throws UsernameNotFoundException {
        try {
            return questionRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UsernameNotFoundException("Could not find question with ID " + id);
        }
    }


    public void save(Question questionInForm, User user) throws UsernameNotFoundException {
        try {
            Question questionInDB = questionRepository.findById(questionInForm.getId()).get();
            questionInDB.setQuestionContent(questionInForm.getQuestionContent());
            questionInDB.setAnswer(questionInForm.getAnswer());
            questionInDB.setApproved(questionInForm.isApproved());

            if (questionInDB.isAnswered()) {
                questionInDB.setAnswerTime(new Date());
                questionInDB.setAnswerer(user);
            }

            questionRepository.save(questionInDB);

        } catch (NoSuchElementException ex) {
            throw new UsernameNotFoundException("Could not find any question with ID " + questionInForm.getId());
        }
    }

    public void approve(Integer id) {
        if(!questionRepository.existsById(id))
            throw new UsernameNotFoundException("Could not find any question with ID " + id);
        questionRepository.updateApprovalStatus(id, true);
    }

    public void disapprove(Integer id) {
        if(!questionRepository.existsById(id))
            throw new UsernameNotFoundException("Could not find any question with ID " + id);
        questionRepository.updateApprovalStatus(id, false);
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        if (!questionRepository.existsById(id)) {
            throw new UsernameNotFoundException("Could not find any question with ID " + id);
        }
        questionRepository.deleteById(id);
    }
}
