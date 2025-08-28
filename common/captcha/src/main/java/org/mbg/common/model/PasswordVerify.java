package org.mbg.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * The PasswordVerify class is a representation of a password verification process.
 * It includes properties to track the number of remaining attempts,
 * success status, and lock time.
 * <p>
 * This class is serializable and provides a no-argument constructor
 * and a constructor with the attempts remaining as a parameter.
 * <p>
 * Note: The serialization ID is defined for consistent serialization purposes.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordVerify implements Serializable {
	@Serial
	private static final long serialVersionUID = -6418103567204222713L;

	private int attemptRemaining;

	private boolean succeed = false;

	private Instant lockTime = Instant.now();

	public PasswordVerify(int attemptRemaining) {
		super();
		
		this.attemptRemaining = attemptRemaining;
	}

}
