package cn.ponfee.web.framework.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Article;

import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

@RestController
@RequestMapping("test")
public class Jsr303Controller {

    /*@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }*/
    
    @PostMapping(path = "article")
    public Object createArticle(@Valid Article article, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> errorList = result.getAllErrors();
            for (ObjectError error : errorList) {
                System.out.println(error.getDefaultMessage());
            }
            return Result.failure(ResultCode.BAD_REQUEST, result.getAllErrors().toString());
        } else {
            return Result.success(article);
        }
    }

}
