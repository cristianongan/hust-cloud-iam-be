package org.mbg.common.security.exception;


import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;

import java.io.Serial;

/**
 * 07/04/2025 - LinhLH: Create new
 *
 * @author LinhLH
 */
public class NoPermissionException extends RuntimeException {

	/** The Constant serialVersionUID */
	@Serial
	private static final long serialVersionUID = -4462714642740568013L;

	public NoPermissionException() {
		super(Labels.getLabels(LabelKey.ERROR_YOU_MIGHT_NOT_HAVE_PERMISSION_TO_PERFORM_THIS_ACTION));
	}

	public NoPermissionException(String message) {
		super(message);
	}

	public NoPermissionException(String message, Throwable t) {
		super(message, t);
	}
}
