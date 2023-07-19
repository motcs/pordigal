package com.prodigal.core.sensitive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 11:20:39 星期二
 */
@Data
public class VocabularyDetection {

    @Schema(title = "检测的文本")
    private String text;

    @Schema(title = "检测的租户uuid")
    private String tenantUuid;

}
