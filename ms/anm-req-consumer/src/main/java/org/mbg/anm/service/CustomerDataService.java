package org.mbg.anm.service;

public interface CustomerDataService {
    void craw(Long id, String api , String token, String dataSource);

    void onFail(Long id);
}
