package com.banmingi.bmshop.gateway.config;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * 拦截白名单
 */
@ConfigurationProperties(prefix = "bmshop.filter")
@Getter
@Setter
public class FilterProperties {
    private List<String> allowPaths;
}
