package com.hust.iam.service;

import com.hust.iam.model.dto.ClientDTO;
import com.hust.iam.model.dto.request.ClientReq;
import com.hust.iam.model.dto.response.SecretRes;
import com.hust.iam.model.dto.response.UserRes;
import com.hust.iam.model.search.UserSearch;
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
