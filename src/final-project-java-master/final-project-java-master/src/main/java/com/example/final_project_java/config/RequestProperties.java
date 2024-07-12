package com.example.final_project_java.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "request")
public class RequestProperties {

   public List<String> permitAllPatterns;

   public List<String> getPermitAllPatterns() {
      return permitAllPatterns;
   }

   public void setPermitAllPatterns(List<String> permitAllPatterns) {
      this.permitAllPatterns = permitAllPatterns;
   }


}












































