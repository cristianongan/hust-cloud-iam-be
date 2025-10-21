package org.mbg.common.security.configuration;

import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.util.SecurityConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author LinhLH
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = SecurityConstants.PropertyPrefix.RSA)
public class RsaProperties {
	private int keyLength;

	private String privateKey;
	
	private String algorithm;

	private Signal signal;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Signal {
		private String publicKey;

		private String privateKey;
	}

	@Primary
	@Bean(name = "rsaProvider")
	public RsaProvider rsaProvider()
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return RsaProvider.fromPrivateKey(algorithm, privateKey);
	}

	@Bean(name = "signalRsaProvider")
	public RsaProvider signalRsaProvider()
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return RsaProvider.fromPrivateKey(algorithm, signal.getPrivateKey());
	}

	@Bean(name = "clientRsaProvider")
	public RsaProvider clientRsaProvider()
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return RsaProvider.fromPublishKey(algorithm, signal.getPublicKey());
	}
}
