package com.haem.blogbackend.common.component;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component("localSignatureValidator")
public class LocalSignatureValidator implements ImageValidator {
    @Override
    public boolean test (byte[] imageBytes) {
        if(imageBytes == null || imageBytes.length < 2){
            return false;
        }

        ByteBuffer buffer = ByteBuffer.wrap(imageBytes);

        if(buffer.get(0) == (byte)0xFF && buffer.get(1) == (byte)0xD8){
            return true;
        }

        if(imageBytes.length >= 8 &&
                buffer.get(0) == (byte)0x89 && buffer.get(1) == (byte)0x50 &&
                buffer.get(2) == (byte)0x4E && buffer.get(3) == (byte)0x47 &&
                buffer.get(4) == (byte)0x0D && buffer.get(5) == (byte)0x0A &&
                buffer.get(6) == (byte)0x1A && buffer.get(7) == (byte)0x0A
        ){
            return true;
        }
        return false;
    }
}
