package com.lessons.linkalias.controller;


import com.lessons.linkalias.service.LinkAliasService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/redirect")
@RequiredArgsConstructor
public class RedirectController {

    @Value("${short.link}")
    private String baseURL;

    private final LinkAliasService linkAliasService;

    @GetMapping("/{link}")
    public String redirectTo(HttpServletRequest request, @PathVariable String link){
        var linkResult = linkAliasService.getRedirect(request.getRequestURI());
        return  "redirect:" +linkResult;
    }
}
