package io.dnajd.projectservice.model

/* The name must match what is stored in the database, or a convertor will have to be created, articles for convertors:
    https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch06.html#types-custom
    https://www.baeldung.com/jpa-persisting-enums-in-jpa
 */

@Suppress("EnumEntryName")
enum class UserAuthorityType {
    project_view,
    project_create,
    project_delete,
    project_edit,
    project_manage_users,
    project_manage_managers,
    project_owner,
}