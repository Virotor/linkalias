package com.lessons.linkalias.controller;


import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.dto.PrettyLinkRequest;
import com.lessons.linkalias.service.LinkAliasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
@Tag(name = "Контроллер для создания коротких ссылок", description = "Контроллер предназначен для создания коротких ссылок")
public class LinkAliasController {

    private final LinkAliasService linkAliasService;

    @PostMapping("/create")
    @Operation(
            summary = "Создание короткой ссылки",
            description = "Позволяет создать простую короткую ссылку"
    )
    public ResponseEntity<?> createShortLink(@Valid @RequestBody LinkRequest linkRequest){
        var shortLink = linkAliasService.createShortLink(linkRequest);
        return ResponseEntity.ok(shortLink);
    }

    @PostMapping("/create/pretty")
    @Operation(
            summary = "Создание короткой именованной ссылки",
            description = "Позволяет создать именованную короткую ссылку"
    )
    public ResponseEntity<?> createPrettyLink(@Valid @RequestBody PrettyLinkRequest linkRequest){
        var shortLink = linkAliasService.createPrettyLink(linkRequest);
        return ResponseEntity.ok(shortLink);
    }

}
