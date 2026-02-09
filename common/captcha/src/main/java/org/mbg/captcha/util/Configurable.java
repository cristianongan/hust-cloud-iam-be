package com.hust.captcha.util;


import lombok.Getter;
import lombok.Setter;

/**
 * Represents an abstract base class to enable configuration management for subclasses.
 * <p>
 * The {@code Configurable} class provides a common structure and utility to manage
 * and utilize configuration properties across various implementations. It includes
 * facilities for setting and retrieving configuration objects.
 * <p>
 * Subclasses can extend this class to incorporate configurable behavior
 * seamlessly into their implementation and leverage shared configuration management.
 * <p>
 * This design promotes reusable and modular code by decoupling configuration concerns
 * from the primary logic of the subclass.
 *
 * @author LinhLH
 * @since 19/04/2025
 */

@Getter
@Setter
public abstract class Configurable {
    private Config config = null;
}
