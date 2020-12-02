package com.cat.origin.modules.dto;

import com.cat.origin.modules.entity.UserEntity;

import java.util.List;

public class UserDto extends UserEntity {

	private static final long serialVersionUID = -184009306207076712L;

	private List<Long> roleIds;

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

}
