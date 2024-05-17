package com.lessons.linkalias.controller;


import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.dto.PrettyLinkRequest;
import com.lessons.linkalias.service.LinkAliasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkAliasController {

    private final LinkAliasService linkAliasService;

    @PostMapping("/create")
    public ResponseEntity<?> createShortLink(@RequestBody LinkRequest linkRequest){
        var shortLink = linkAliasService.createShortLink(linkRequest);
        return ResponseEntity.ok(shortLink);
    }

    @PostMapping("/create/pretty")
    public ResponseEntity<?> createPrettyLink(@RequestBody PrettyLinkRequest linkRequest){
        var shortLink = linkAliasService.createPrettyLink(linkRequest);
        return ResponseEntity.ok(shortLink);
    }

}
