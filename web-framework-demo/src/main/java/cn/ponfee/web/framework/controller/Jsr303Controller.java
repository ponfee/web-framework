package cn.ponfee.web.framework.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Article;
import code.ponfee.commons.model.Result;

@RestController
@RequestMapping("test")
@Validated
public class Jsr303Controller {

    /*@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }*/

    /**
     * 不校验
     * 
     * @param article
     * @param result
     * @return
     */
    @PostMapping(path = "article0")
    public Result<Article> article0(Article article, BindingResult result) {
        return Result.success(article);
    }

    /**
     * {@link code.ponfee.commons.constrain.Jsr303Validator#verify(org.aspectj.lang.ProceedingJoinPoint, BindingResult)}
     * 
     * @param article
     * @param result
     * @return
     */
    @PostMapping(path = "article1")
    public Result<Article> article1(@Valid Article article, BindingResult result) {
        /*if (result.hasErrors()) {
            System.out.println(result.getAllErrors().toString());
            String errorMsg = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",", "[", "]"));
            return Result.failure(ResultCode.BAD_REQUEST, errorMsg);
        } else {
            return Result.success(article);
        }*/
        return Result.success(article);
    }

    /**
     * {@link code.ponfee.commons.web.AbstractWebExceptionHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)}
     * 
     * @param article
     * @return
     */
    @PostMapping(path = "article2")
    public Result<Article> article2(@Validated Article article) {
        return Result.success(article);
    }


    /**
     * {@link code.ponfee.commons.constrain.Jsr303Validator#verify(org.aspectj.lang.ProceedingJoinPoint, BindingResult)}
     * 
     * @param article
     * @param result
     * @return
     */
    @PostMapping(path = "article3")
    public Result<Article> article3(@RequestBody @Validated Article article, BindingResult result) {
        return Result.success(article);
    }

    /**
     * {@link code.ponfee.commons.web.AbstractWebExceptionHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.bind.MethodArgumentNotValidException)}
     * @param article
     * @return
     */
    @PostMapping(path = "article4")
    public Result<Article> article4(@RequestBody @Validated Article article) {
        return Result.success(article);
    }

    /**
     * XXX Error:
     *   java.lang.IllegalStateException: An Errors/BindingResult argument is expected to be declared immediately after the model attribute, 
     *   the @RequestBody or the @RequestPart arguments to which they apply: 
     *   public code.ponfee.commons.model.Result cn.ponfee.web.framework.controller.Jsr303Controller.article5(int,org.springframework.validation.BindingResult)
     * 
     * 这种方式会报错
     * 
     * @param grade
     * @param result
     * @return
     */
    @PostMapping(path = "article5")
    public Result<Article> article5(@Range(min = 1, max = 9, message = "年级只能从1-9")
                                    @RequestParam(name = "grade")
                                    int grade, 
                                    BindingResult result) {
        return Result.success(null);
    }

    /**
     * {@link code.ponfee.commons.web.AbstractWebExceptionHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.validation.ConstraintViolationException)}
     * 
     * @param grade
     * @return
     */
    @PostMapping(path = "article6")
    public Result<Article> article6(@Range(min = 1, max = 9, message = "年级只能从1-9") @RequestParam(name = "grade") int grade,
                                    @Min(value = 1, message = "班级最小只能1") @Max(value = 99, message = "班级最大只能99") @RequestParam(name = "classroom") int classroom) {
        return Result.success(null);
    }

}
