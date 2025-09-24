package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.request.ClientReq;
import org.mbg.anm.model.dto.request.ClientTokenReq;
import org.mbg.anm.model.dto.response.SecretRes;
import org.mbg.anm.model.dto.response.UserRes;
import org.mbg.anm.model.search.UserSearch;
import org.springframework.data.domain.Page;

public interface ClientService {

    SecretRes createClient(ClientDTO clientDTO);

    void disableClient(ClientReq clientReq);

    void enableClient(ClientReq clientReq);

    SecretRes resetSecret(ClientReq clientReq);

    void deleteClient(ClientReq clientReq);

    Page<ClientDTO> search(ClientReq clientReq);

    Page<UserRes> searchUser(UserSearch clientReq);

    void assignUser(ClientReq clientReq);
}
