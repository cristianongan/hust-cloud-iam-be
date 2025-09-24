package org.mbg.anm.repository.extend;

import org.mbg.anm.model.Client;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.dto.request.ClientReq;
import org.mbg.anm.model.dto.request.RoleReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientRepositoryExtend {
    List<Client> search(ClientReq clientReq, Pageable pageable);

    Long count (ClientReq clientReq);
}
