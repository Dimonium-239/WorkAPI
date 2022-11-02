package com.github.dimonium_239.workuaapi.firebase.dto;

import lombok.Getter;
import lombok.Setter;

public class Paging {

    @Getter @Setter
    private Cursors cursors;

    @Getter @Setter
    private String next;

    @Getter @Setter
    private String previous;
}
