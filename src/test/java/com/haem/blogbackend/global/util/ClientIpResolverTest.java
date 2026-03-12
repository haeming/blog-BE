package com.haem.blogbackend.global.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ClientIpResolverTest {

    private final ClientIpResolver clientIpResolver = new ClientIpResolver();

    @Test
    void spoofableHeadersDoNotOverrideRemoteAddr() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("10.0.0.1");
        request.addHeader("X-Forwarded-For", "203.0.113.10");
        request.addHeader("X-Real-IP", "198.51.100.20");

        String resolvedIp = clientIpResolver.resolve(request);

        assertThat(resolvedIp).isEqualTo("10.0.0.1");
    }
}
