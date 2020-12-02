package com.cat.origin.modules.dto;

import com.cat.origin.modules.entity.RoleEntity;

import java.util.List;

public class RoleDto extends RoleEntity {

	private static final long serialVersionUID = 4218495592167610193L;

	private List<Long> permissionIds;

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
