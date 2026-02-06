package com.hust.common.annotation;

/**
 * Enumerates different levels of permission that can be assigned within a system to
 * define varying levels of access and control over specific operations or resources.
 */
public enum PermissionLevel {
	/**
	 * Represents the highest level of permission, typically granting full access and control over all operations
	 * or resources within a specific context.
	 */
	OWNER,
	/**
	 * Represents a permission level that restricts access to operations or resources
	 * within a specific geographical or organizational area.
	 */
	AREA,
	
	/**
	 * Represents a permission level that restricts access to operations or resources
	 * within a specific department in an organization or system.
	 */
    DEPARTMENT,

	/**
	 * Represents a permission level that restricts access to operations or resources
	 * within the scope of a specific project in an organization or system.
	 */
	PROJECT,

	/**
	 * Represents a permission level that restricts access to operations or resources
	 * within the context of a specific building in an organization or system.
	 */
	BUILDING
}
