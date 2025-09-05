package org.mbg.anm.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.Client;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.request.ClientTokenReq;
import org.mbg.anm.service.ClientService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.label.LabelKey;
import org.mbg.common.util.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("token")
    public ResponseEntity<?> createToken(HttpServletRequest servletRequest, @RequestBody ClientTokenReq clientDTO) {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        String auth =  request.getHeader("Authorization");
        String[] rs = HeaderUtil.decodeBasicAuthorization(auth);

        if (Validator.isNull(rs) || rs.length != 2) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_CREDENTIAL, Client.class.getName(), LabelKey.ERROR_INVALID_CREDENTIAL);
        }

        clientDTO.setClientId(rs[0]);
        clientDTO.setClientSecret(rs[1]);

        return ResponseEntity.ok(this.clientService.token(clientDTO));
    }

}
