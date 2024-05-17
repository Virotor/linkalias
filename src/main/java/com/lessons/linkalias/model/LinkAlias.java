package com.lessons.linkalias.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alias")
@EqualsAndHashCode
public class LinkAlias {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "base_link", unique = true)
    private String baseLink;

    @Column(name = "short_link", unique = true)
    private String shortLink;

    @EqualsAndHashCode.Exclude
    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "ttl")
    private Long ttl;


}
