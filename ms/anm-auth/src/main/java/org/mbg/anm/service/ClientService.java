package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.request.ClientTokenReq;

public interface ClientService {

    JwtAccessToken token(ClientTokenReq clientDTO);

    JwtAccessToken refresh(String refreshToken);

    ClientDTO createClient(ClientDTO clientDTO);

    String getSecret(String clientId);
}
