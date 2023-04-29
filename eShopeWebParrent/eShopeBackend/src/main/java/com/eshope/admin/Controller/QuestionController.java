package com.eshope.admin.Controller;

import com.eShope.common.entity.Question;

import com.eShope.common.entity.Review;
import com.eshope.admin.Security.EshopeUserDetails;
import com.eshope.admin.Service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions")
    public String listAllQuestions(Model model) {
        return listByPage(1, model, "askTime", "desc", null);
    }

    @GetMapping("/questions/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
        Page<Question> page = questionService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Question> listQuestions = page.getContent();

        long startCount = (pageNum - 1) * questionService.QUESTIONS_PER_PAGE + 1;
        long endCount = startCount + questionService.QUESTIONS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllQuestions", listQuestions);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        return "Questions/questions";
    }

    @GetMapping("/questions/detail/{id}")
    public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Question question = questionService.getById(id);
            model.addAttribute("question", question);

            return "Questions/viewQuestionModal";
        } catch (UsernameNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
        }
    }

    @GetMapping("/questions/edit/{id}")
    public String editQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Question question = questionService.getById(id);
            model.addAttribute("question", question);
//            model.addAttribute("pageTitle", "Edit Question (ID: " + id + ")");

            return "Questions/questionUpdateForm";
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
        }
    }

    @PostMapping("/questions/save")
    public String saveQuestion(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "question") Question question, Errors errors, Model model, @AuthenticationPrincipal EshopeUserDetails userDetails) {

        Integer id= question.getId();
        log.error(String.valueOf(id));
        Question existingQuestion=questionService.getById(id);
        question.setProduct(existingQuestion.getProduct());
        question.setAsker(existingQuestion.getAsker());
        question.setAskTime(existingQuestion.getAskTime());
        //DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            log.error("New Review form validation failed due to : " + errors.toString());
            model.addAttribute("question", question);
            return "Questions/questionUpdateForm";
        }

        //SAVE DETAILS
        try {

            questionService.save(question, userDetails.getUser());
            redirectAttributes.addFlashAttribute("message", "The Question ID " + question.getId() + " has been updated successfully.");
        } catch (UsernameNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", "Could not find any question with ID " + question.getId());
        }
        return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
    }

    @GetMapping("/questions/{id}/approve")
    public String approveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            questionService.approve(id);
            ra.addFlashAttribute("message", "The Question ID " + id + " has been approved.");
        }
        catch (UsernameNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
    }

    @GetMapping("/questions/{id}/disapprove")
    public String disapproveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            questionService.disapprove(id);
            ra.addFlashAttribute("message", "The Question ID " + id + " has been disapproved.");
        }
        catch (UsernameNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
    }

    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable(name = "id") Integer id, RedirectAttributes ra) throws UsernameNotFoundException {

        try {
            questionService.delete(id);

            ra.addFlashAttribute("message", String.format("The question ID %d has been deleted successfully.", id));
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
    }


}

