package com.agungdh.bpkad.sipdtosimdamigrator.resources;

import com.agungdh.bpkad.sipdtosimdamigrator.services.SipdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sipd")
@RequiredArgsConstructor
public class SipdResource {
    @Autowired
    SipdService sipdService;

    @GetMapping("/listopd")
    public void listopd() {
        sipdService.getSipdOptions();
    }

    @GetMapping("/listopdunitonly")
    public void listopdunitonly() {
        sipdService.getSipdOptionsBasedOnUnit();
    }
}
