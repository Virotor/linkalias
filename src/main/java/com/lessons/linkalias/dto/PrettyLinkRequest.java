package com.lessons.linkalias.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrettyLinkRequest {

    private String url;
    private  String prettyUrl;
    private Long TTL;
}
