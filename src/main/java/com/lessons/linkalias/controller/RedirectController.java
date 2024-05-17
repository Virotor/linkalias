package com.lessons.linkalias.controller;


import com.lessons.linkalias.service.LinkAliasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Контроллер для перехода по коротким ссылка", description = "Контроллер предназначен для перехода по созданным коротким ссылкам")
public class RedirectController {

    @Value("${short.link}")
    private String baseURL;

    private final LinkAliasService linkAliasService;

    @GetMapping("/{link}")
    @Operation(
            summary = "Переход по ссылке",
            description = "Позволяет перейти по созданной короткой ссылке"
    )
    public String redirectTo(HttpServletRequest request, @PathVariable String link){
        var linkResult = linkAliasService.getRedirect(request.getRequestURI());
        return  "redirect:" +linkResult;
    }
}
