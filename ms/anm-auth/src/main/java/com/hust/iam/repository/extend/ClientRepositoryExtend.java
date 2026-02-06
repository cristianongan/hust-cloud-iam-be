package com.hust.iam.repository.extend;

import com.hust.iam.model.Client;
import com.hust.iam.model.dto.request.ClientReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientRepositoryExtend {
    List<Client> search(ClientReq clientReq, Pageable pageable);

    Long count (ClientReq clientReq);
}
