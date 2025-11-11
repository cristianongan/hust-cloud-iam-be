package org.mbg.anm.service;

import org.mbg.common.base.util.PiiScanner;
import org.mbg.common.util.Validator;

import java.util.List;
import java.util.Map;

public interface CustomerDataService {
    void craw(Long id, String api , String token, String dataSource);

    void onFail(Long id);
}
