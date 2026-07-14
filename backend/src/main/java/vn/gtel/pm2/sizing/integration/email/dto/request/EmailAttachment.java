package vn.gtel.pm2.sizing.integration.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
    private String fileName;
    private String contentType;
    private byte[] content;
}