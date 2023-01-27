package com.mysite.sbb.question;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {

	 private final QuestionService questionService;
	 private final UserService userService;
	
//    @GetMapping("/list")
//    public String list(Model model) {
//    	List<Question> questionList = this.questionService.getList();
//    	model.addAttribute("questionList",questionList);
//        return "question_list";
//    }
    
    @GetMapping("/list")
    public String list(Model model
    		,@RequestParam(value="page",defaultValue = "0") int page) {
    	Page<Question> paging = this.questionService.getList(page);
    	model.addAttribute("paging",paging);
    	return "question_list";
    }
    

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, AnswerForm answerForm
    		, @PathVariable("id") Integer id) {
    	 Question question = this.questionService.getQuestion(id);
    	 model.addAttribute("question", question);
        return "question_detail";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm
    		,BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // TODO 질문을 저장한다.
    	 this.questionService.create(questionForm.getSubject()
    			 ,questionForm.getContent(),siteUser);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }
}
