package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.service.CustomerService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
}
