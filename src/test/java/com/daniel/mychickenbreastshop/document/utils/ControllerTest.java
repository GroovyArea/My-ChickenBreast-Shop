package com.daniel.mychickenbreastshop.document.utils;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.global.util.ParamConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
public abstract class ControllerTest {

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    private ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected String createJson(Object object) {
        return JsonUtil.objectToString(object);
    }

    protected MultiValueMap<String, String> createParams(Object dto) {
        return ParamConverter.convert(objectMapper, dto);
    }

}
