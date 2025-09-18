package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.model.Client;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.request.ClientTokenReq;
import org.mbg.anm.repository.ClientRepository;
import org.mbg.anm.service.ClientService;
import org.mbg.anm.service.TokenService;
import org.mbg.anm.service.mapper.ClientMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.label.LabelKey;
import org.mbg.common.util.Validator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMapper clientMapper;

    private final ClientRepository clientRepository;

    private final JwtProvider jwtProvider;

    private final TokenService tokenService;


    @Override
    public JwtAccessToken token(ClientTokenReq clientDTO) {
        if (Validator.isNull(clientDTO.getClientId()) || Validator.isNull(clientDTO.getClientSecret())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_CREDENTIAL, Client.class.getName(), LabelKey.ERROR_INVALID_CREDENTIAL);
        }

        Client client = this.clientRepository.findByClientId(clientDTO.getClientId());

        if (Validator.isNull(client) || !Validator.equals(client.getStatus(), EntityStatus.ACTIVE)) {
            throw new BadRequestException(LabelKey.ERROR_CLIENT_NOT_FOUND, Client.class.getName(), LabelKey.ERROR_CLIENT_NOT_FOUND);
        }

        if (!Validator.equals(client.getClientSecret(), clientDTO.getClientSecret())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_CREDENTIAL, Client.class.getName(), LabelKey.ERROR_INVALID_CREDENTIAL);
        }

        return jwtProvider.createAccessToken(clientDTO.getClientId());
    }

    @Override
    public JwtAccessToken refresh(String refreshToken) {
        if (Validator.isNull(refreshToken)) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_CREDENTIAL, Client.class.getName(), LabelKey.ERROR_INVALID_CREDENTIAL);
        }

        this.jwtProvider.validateToken(refreshToken);

        String clientId = this.jwtProvider.getSubject(refreshToken);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(LabelKey.ERROR_CLIENT_NOT_FOUND, Client.class.getName(), LabelKey.ERROR_CLIENT_NOT_FOUND);
        }

        Client client = this.clientRepository.findByClientId(clientId);

        if (Validator.isNull(client)) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_CREDENTIAL, Client.class.getName(), LabelKey.ERROR_INVALID_CREDENTIAL);
        }

        return null;
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        return null;
    }

    @Override
    public String getSecret(String clientId) {
        return "";
    }
}
