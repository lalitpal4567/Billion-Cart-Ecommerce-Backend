package com.billioncart.payload;

import lombok.Data;

@Data
public class AccountRoleRequest {
	private Long accountId;
	private Long roleId;
}
