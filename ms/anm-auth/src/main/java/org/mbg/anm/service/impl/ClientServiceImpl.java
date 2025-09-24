package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.model.*;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.anm.model.dto.UserDTO;
import org.mbg.anm.model.dto.request.ClientReq;
import org.mbg.anm.model.dto.request.ClientTokenReq;
import org.mbg.anm.model.dto.response.SecretRes;
import org.mbg.anm.model.dto.response.UserRes;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.repository.ClientRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.anm.service.ClientService;
import org.mbg.anm.service.TokenService;
import org.mbg.anm.service.mapper.ClientMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.RandomGenerator;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMapper clientMapper;

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public SecretRes createClient(ClientDTO clientDTO) {
        if (Validator.isNull(clientDTO.getClientName())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[]{Labels.getLabels(LabelKey.LABEL_SOURCE_NAME)}),
                    ClientDTO.class.getName(),LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        Client client = new Client();
        client.setClientName(clientDTO.getClientName());

        if (Validator.isNull(clientDTO.getClientId())) {
            clientDTO.setClientId(RandomGenerator.generateRandomAlphabet(32, true));
        }

        client.setClientId(clientDTO.getClientId());
        client.setClientSecret(RandomGenerator.generateRandomAlphabet(32, true));

        if (Validator.isNull(clientDTO.getUserId())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[]{Labels.getLabels(LabelKey.LABEL_USER)}),
                    ClientDTO.class.getName(),LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        if (!this.userRepository.existsByIdAndStatus(clientDTO.getUserId(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND),
                    ClientDTO.class.getName(),LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        client.setUserId(clientDTO.getUserId());
        client.setStatus(EntityStatus.ACTIVE.getStatus());

        this.clientRepository.save(client);

        return SecretRes.builder().secret(client.getClientSecret()).build();
    }

    @Override
    @Transactional
    public void disableClient(ClientReq clientReq) {
        if (Validator.isNotNull(clientReq.getIds())) {
            this.clientRepository.updateStatusByClientIds(clientReq.getIds(), EntityStatus.ACTIVE.getStatus());
        }
    }

    @Override
    @Transactional
    public void enableClient(ClientReq clientReq) {
        if (Validator.isNotNull(clientReq.getIds())) {
            this.clientRepository.updateStatusByClientIds(clientReq.getIds(), EntityStatus.ACTIVE.getStatus());
        }
    }

    @Override
    @Transactional
    public SecretRes resetSecret(ClientReq clientReq) {
        Client client = this.getClient( clientReq.getClientId() );
        client.setClientSecret(RandomGenerator.generateRandomAlphabet(32, true));
        this.clientRepository.save(client);

        return SecretRes.builder().secret(client.getClientSecret()).build();
    }

    @Override
    @Transactional
    public void deleteClient(ClientReq clientReq) {
        if (Validator.isNotNull(clientReq.getIds())) {
            this.clientRepository.updateStatusByClientIds(clientReq.getIds(), EntityStatus.DELETED.getStatus());
        }
    }

    @Override
    public Page<ClientDTO> search(ClientReq clientReq) {
        Pageable pageable = PageRequest.of(clientReq.getPage(), clientReq.getPageSize());

        List<Client> roles = this.clientRepository.search(clientReq, pageable);

        List<ClientDTO> content = this.clientMapper.toDto(roles);

        Long count  = this.clientRepository.count(clientReq);

        List<Long> ids = content.stream().map(ClientDTO::getUserId).filter(Validator::isNotNull).toList();

        List<User> users = this.userRepository.findByIdIn(ids);
        Map<Long, String> userMap =
                users.stream()
                        .collect(Collectors.toMap(User::getId, User::getUsername));

        content.forEach(item -> {
            item.setUsername(userMap.getOrDefault(item.getUserId(), null));
        });

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<UserRes> searchUser(UserSearch clientReq) {
        Pageable pageable = PageRequest.of(clientReq.getPage(), clientReq.getPageSize());

        List<User> users = this.userRepository.search(clientReq, pageable);

        Long count  = this.userRepository.count(clientReq);

        List<UserRes> content = users.stream().map(user -> UserRes.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build()).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    @Transactional
    public void assignUser(ClientReq clientReq) {
        if (Validator.isNull(clientReq.getUserId())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[]{Labels.getLabels(LabelKey.LABEL_USER)}),
                    ClientDTO.class.getName(),LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        if (!this.userRepository.existsByIdAndStatus(clientReq.getUserId(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND),
                    ClientDTO.class.getName(),LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        Client client = this.getClient( clientReq.getClientId() );

        if (Validator.isNull(client.getClientId())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_CLIENT_NOT_FOUND),
                    ClientDTO.class.getName(),LabelKey.ERROR_CLIENT_NOT_FOUND);
        }

        client.setUserId(clientReq.getUserId());
        this.clientRepository.save(client);
    }

    private Client getClient(String clientId) {
        Client client = this.clientRepository.findByClientId(clientId);

        if (Validator.isNull(client) || Validator.equals(client.getStatus(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_CLIENT_NOT_FOUND),
                    ClientDTO.class.getName(),LabelKey.ERROR_CLIENT_NOT_FOUND);
        }

        return client;
    }
}
