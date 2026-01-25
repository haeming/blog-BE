package com.haem.blogbackend.common.component;

import org.springframework.stereotype.Component;

@Component("webpSignatureValidator")
public class WebpSignatureValidator implements ImageValidator {
    @Override
    public boolean test(byte[] bytes) {
        return bytes.length >= 12 &&
                bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F' &&
                bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P';
    }
}
