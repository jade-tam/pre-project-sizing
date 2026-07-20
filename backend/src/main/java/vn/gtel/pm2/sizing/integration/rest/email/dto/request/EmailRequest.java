package vn.gtel.pm2.sizing.integration.rest.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    private String to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String textBody;
    private String htmlBody;
    private List<EmailAttachment> attachments;
    private String templateId;
    private Map<String, Object> templateVariables;
}
