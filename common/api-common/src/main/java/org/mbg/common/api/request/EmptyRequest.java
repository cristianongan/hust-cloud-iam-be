package org.mbg.common.api.request;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EmptyRequest extends Request {

    /** The Constant serialVersionUID */
    @Serial
    private static final long serialVersionUID = 4437315325843201324L;

}
