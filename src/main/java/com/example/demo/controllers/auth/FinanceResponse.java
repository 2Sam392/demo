package com.example.demo.controllers.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinanceResponse {
        private long id;
        private String studentId;
        private boolean hasOutstandingBalance;
        private Map<String,Map<String,String>> _links;

}
