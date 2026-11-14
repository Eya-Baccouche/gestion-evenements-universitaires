package com.warriors.sponsors.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaterielRestitueMessage {

    private Long evenementId;
    private Long equipementId;
}
