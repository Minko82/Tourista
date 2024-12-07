package com.supercoolproject.tourista.controlleradvice;

import com.supercoolproject.tourista.exception.ControllerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 *  Source: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc#controller-based-exception-handling
 */
@ControllerAdvice
class GlobalDefaultExceptionHandler {
    // Can replace this with Lombok `@Slf4j`
    private static final Logger log = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = ControllerException.class)
    public ModelAndView customErrorHandler(HttpServletRequest req, ControllerException e) throws Exception {
        log.warn("{}", req.getRequestURL());
        log.warn("Exception: ", e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Controller Exception");
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setStatus(e.getHttpStatus());
        mav.setViewName(DEFAULT_ERROR_VIEW);

        return mav;
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ModelAndView notFoundHandler(HttpServletRequest req, Exception e) {
        log.warn("{}", req.getRequestURL());
        log.warn("Exception: ", e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Not Found");
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        log.warn("{}", req.getRequestURL());
        log.warn("Exception: ", e);

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Something went wrong. Our engineers will work on it.");
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}