package com.system.lms.fo.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addFullPath(HttpServletRequest request, Model model) {
        String commonUri = request.getRequestURI();

        model.addAttribute("commonUri", commonUri);
    }
}